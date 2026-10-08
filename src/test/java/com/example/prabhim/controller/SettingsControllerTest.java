package com.example.prabhim.controller;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.prabhim.dto.LoginRequest;
import com.example.prabhim.dto.RegisterRequest;
import com.example.prabhim.dto.settings.SettingsDTO;
import com.example.prabhim.entity.GymSettings;
import com.example.prabhim.entity.User;
import com.example.prabhim.repository.SettingsRepository;
import com.example.prabhim.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class SettingsControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private com.example.prabhim.repository.LeadRepository leadRepository;

    private String authToken;
    private UUID currentUserId;

    private String createAndLoginUser(String email, String password, String firstName, String lastName) throws Exception {
        RegisterRequest registerReq = new RegisterRequest();
        registerReq.setEmail(email);
        registerReq.setPassword(password);
        registerReq.setPasswordConfirm(password);
        registerReq.setFirstName(firstName);
        registerReq.setLastName(lastName);

        mockMvc.perform(post("/api/v1/auth/register/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isCreated());

        User user = userRepository.findByEmail(email).orElseThrow();
        user.setEmailVerified(true);
        user.setStaff(true);
        userRepository.save(user);

        LoginRequest loginReq = new LoginRequest(email, password);
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode json = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        return json.get("data").get("access").asText();
    }

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        leadRepository.deleteAll();
        settingsRepository.deleteAll();
        userRepository.deleteAll();

        this.authToken = createAndLoginUser("owner@gymos.com", "GymOS@12345", "Alex", "Vance");
        User user = userRepository.findByEmail("owner@gymos.com").orElseThrow();
        this.currentUserId = user.getId();
    }

    @Test
    @DisplayName("1. GET /api/v1/settings without JWT returns 401 Unauthorized")
    void testGetSettingsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/settings"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success", is(false)));

        mockMvc.perform(get("/api/settings"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    @DisplayName("2 & 3. First GET creates and returns sensible GymOS default settings")
    void testGetSettingsDefaultCreation() throws Exception {
        // First GET should create default settings
        mockMvc.perform(get("/api/v1/settings")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.general.currency", is("INR")))
                .andExpect(jsonPath("$.data.general.timezone", is("Asia/Kolkata")))
                .andExpect(jsonPath("$.data.membership.defaultMembershipDurationDays", is(30)))
                .andExpect(jsonPath("$.data.membership.membershipExpiryReminderDays", is(7)))
                .andExpect(jsonPath("$.data.attendance.attendanceEnabled", is(true)))
                .andExpect(jsonPath("$.data.attendance.lateEntryMinutes", is(15)))
                .andExpect(jsonPath("$.data.trainer.trainerCommissionEnabled", is(false)))
                .andExpect(jsonPath("$.data.workout.workoutPlansEnabled", is(true)))
                .andExpect(jsonPath("$.data.nutrition.nutritionPlansEnabled", is(true)))
                .andExpect(jsonPath("$.data.payment.defaultPaymentMethod", is("UPI")))
                .andExpect(jsonPath("$.data.invoice.invoicePrefix", is("INV-")))
                .andExpect(jsonPath("$.data.notification.emailNotificationsEnabled", is(true)))
                .andExpect(jsonPath("$.data.security.sessionTimeoutMinutes", is(30)))
                .andExpect(jsonPath("$.data.appearance.theme", is("light")));

        // Verify in database
        assertTrue(settingsRepository.findByUserId(currentUserId).isPresent());
        GymSettings saved = settingsRepository.findByUserId(currentUserId).get();
        assertEquals("INR", saved.getCurrency());
        assertEquals("Asia/Kolkata", saved.getTimezone());
        assertEquals(30, saved.getDefaultMembershipDurationDays());
    }

    @Test
    @DisplayName("4 & 5. PUT valid settings updates database and GET returns updated values")
    void testPutAndGetSettings() throws Exception {
        SettingsDTO updateDto = new SettingsDTO();

        SettingsDTO.GymProfileDTO profile = new SettingsDTO.GymProfileDTO();
        profile.setGymName("Iron Fortress Fitness");
        profile.setEmail("contact@ironfortress.com");
        profile.setPhone("+91 9876543210");
        profile.setAddress("42 Fitness Boulevard");
        profile.setCity("Mumbai");
        profile.setState("Maharashtra");
        profile.setPincode("400001");
        profile.setCountry("India");
        profile.setWebsite("https://ironfortress.com");
        updateDto.setGymProfile(profile);

        SettingsDTO.GeneralDTO general = new SettingsDTO.GeneralDTO();
        general.setCurrency("USD");
        general.setTimezone("America/New_York");
        general.setDateFormat("DD/MM/YYYY");
        general.setLanguage("English");
        updateDto.setGeneral(general);

        SettingsDTO.MembershipDTO membership = new SettingsDTO.MembershipDTO();
        membership.setDefaultMembershipDurationDays(90);
        membership.setMembershipExpiryReminderDays(14);
        membership.setAutoRenewalEnabled(true);
        membership.setAllowFreeze(true);
        membership.setMaxFreezeDays(45);
        updateDto.setMembership(membership);

        SettingsDTO.PaymentDTO payment = new SettingsDTO.PaymentDTO();
        payment.setDefaultPaymentMethod("Card");
        payment.setTaxEnabled(true);
        payment.setTaxRate(new BigDecimal("18.00"));
        updateDto.setPayment(payment);

        SettingsDTO.AppearanceDTO appearance = new SettingsDTO.AppearanceDTO();
        appearance.setTheme("dark");
        updateDto.setAppearance(appearance);

        // Perform PUT
        mockMvc.perform(put("/api/v1/settings")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.message", is("Settings saved successfully.")))
                .andExpect(jsonPath("$.data.updatedAt", notNullValue()))
                .andExpect(jsonPath("$.data.settings.gymProfile.gymName", is("Iron Fortress Fitness")))
                .andExpect(jsonPath("$.data.settings.general.currency", is("USD")))
                .andExpect(jsonPath("$.data.settings.membership.defaultMembershipDurationDays", is(90)))
                .andExpect(jsonPath("$.data.settings.payment.taxRate", is(18.0)))
                .andExpect(jsonPath("$.data.settings.appearance.theme", is("dark")));

        // Perform GET to confirm persistence
        mockMvc.perform(get("/api/v1/settings")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.gymProfile.gymName", is("Iron Fortress Fitness")))
                .andExpect(jsonPath("$.data.gymProfile.email", is("contact@ironfortress.com")))
                .andExpect(jsonPath("$.data.general.currency", is("USD")))
                .andExpect(jsonPath("$.data.general.timezone", is("America/New_York")))
                .andExpect(jsonPath("$.data.membership.defaultMembershipDurationDays", is(90)))
                .andExpect(jsonPath("$.data.membership.autoRenewalEnabled", is(true)))
                .andExpect(jsonPath("$.data.payment.taxRate", is(18.0)))
                .andExpect(jsonPath("$.data.appearance.theme", is("dark")));
    }

    @Test
    @DisplayName("6. Partial PUT updates only supplied fields and preserves omitted fields")
    void testPartialUpdateSettings() throws Exception {
        // First, set known settings
        SettingsDTO initial = new SettingsDTO();
        SettingsDTO.MembershipDTO mem = new SettingsDTO.MembershipDTO();
        mem.setDefaultMembershipDurationDays(30);
        mem.setMembershipExpiryReminderDays(7);
        mem.setMaxFreezeDays(15);
        initial.setMembership(mem);

        SettingsDTO.GeneralDTO gen = new SettingsDTO.GeneralDTO();
        gen.setCurrency("INR");
        gen.setTimezone("Asia/Kolkata");
        initial.setGeneral(gen);

        mockMvc.perform(put("/api/v1/settings")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(initial)))
                .andExpect(status().isOk());

        // Partial update: only change defaultMembershipDurationDays
        SettingsDTO partial = new SettingsDTO();
        SettingsDTO.MembershipDTO partialMem = new SettingsDTO.MembershipDTO();
        partialMem.setDefaultMembershipDurationDays(60);
        partial.setMembership(partialMem);

        mockMvc.perform(put("/api/v1/settings")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partial)))
                .andExpect(status().isOk());

        // Verify that defaultMembershipDurationDays is 60, but reminder days (7), maxFreezeDays (15), currency (INR) are unchanged
        mockMvc.perform(get("/api/v1/settings")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.membership.defaultMembershipDurationDays", is(60)))
                .andExpect(jsonPath("$.data.membership.membershipExpiryReminderDays", is(7)))
                .andExpect(jsonPath("$.data.membership.maxFreezeDays", is(15)))
                .andExpect(jsonPath("$.data.general.currency", is("INR")))
                .andExpect(jsonPath("$.data.general.timezone", is("Asia/Kolkata")));
    }

    @Test
    @DisplayName("7. Invalid email format returns HTTP 400")
    void testInvalidEmail() throws Exception {
        SettingsDTO invalid = new SettingsDTO();
        SettingsDTO.GymProfileDTO profile = new SettingsDTO.GymProfileDTO();
        profile.setEmail("invalid-email-format");
        invalid.setGymProfile(profile);

        mockMvc.perform(put("/api/v1/settings")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    @DisplayName("8. Invalid phone format returns HTTP 400")
    void testInvalidPhone() throws Exception {
        SettingsDTO invalid = new SettingsDTO();
        SettingsDTO.GymProfileDTO profile = new SettingsDTO.GymProfileDTO();
        profile.setPhone("123");
        invalid.setGymProfile(profile);

        mockMvc.perform(put("/api/v1/settings")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    @DisplayName("9. Invalid tax rate / commission percentage returns HTTP 400")
    void testInvalidPercentages() throws Exception {
        // Negative tax rate
        SettingsDTO invalidTax = new SettingsDTO();
        SettingsDTO.PaymentDTO payment = new SettingsDTO.PaymentDTO();
        payment.setTaxRate(new BigDecimal("-5.00"));
        invalidTax.setPayment(payment);

        mockMvc.perform(put("/api/v1/settings")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTax)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)));

        // Tax rate > 100
        payment.setTaxRate(new BigDecimal("105.00"));
        mockMvc.perform(put("/api/v1/settings")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTax)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)));

        // Commission > 100
        SettingsDTO invalidCommission = new SettingsDTO();
        SettingsDTO.TrainerDTO trainer = new SettingsDTO.TrainerDTO();
        trainer.setDefaultCommissionPercentage(120);
        invalidCommission.setTrainer(trainer);

        mockMvc.perform(put("/api/v1/settings")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCommission)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    @DisplayName("10. Invalid membership and security numbers return HTTP 400")
    void testInvalidDurations() throws Exception {
        SettingsDTO invalid = new SettingsDTO();
        SettingsDTO.MembershipDTO mem = new SettingsDTO.MembershipDTO();
        mem.setDefaultMembershipDurationDays(0);
        invalid.setMembership(mem);

        mockMvc.perform(put("/api/v1/settings")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)));

        SettingsDTO invalidSec = new SettingsDTO();
        SettingsDTO.SecurityDTO sec = new SettingsDTO.SecurityDTO();
        sec.setSessionTimeoutMinutes(-10);
        invalidSec.setSecurity(sec);

        mockMvc.perform(put("/api/v1/settings")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidSec)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    @DisplayName("11. Relogin allows access to persisted settings")
    void testReloginSettingsPersistence() throws Exception {
        // Update setting
        SettingsDTO dto = new SettingsDTO();
        SettingsDTO.GymProfileDTO profile = new SettingsDTO.GymProfileDTO();
        profile.setGymName("Pulse Performance");
        dto.setGymProfile(profile);

        mockMvc.perform(put("/api/v1/settings")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // Re-login
        LoginRequest loginReq = new LoginRequest("owner@gymos.com", "GymOS@12345");
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode json = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        String newAuthToken = json.get("data").get("access").asText();

        // Check settings with new token
        mockMvc.perform(get("/api/v1/settings")
                        .header("Authorization", "Bearer " + newAuthToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.gymProfile.gymName", is("Pulse Performance")));
    }

    @Test
    @DisplayName("12. Verify tenant / user isolation between two different gym owners")
    void testMultiTenantIsolation() throws Exception {
        // User 1 sets gym name to Gym Alpha
        SettingsDTO dto1 = new SettingsDTO();
        SettingsDTO.GymProfileDTO profile1 = new SettingsDTO.GymProfileDTO();
        profile1.setGymName("Gym Alpha");
        dto1.setGymProfile(profile1);

        mockMvc.perform(put("/api/v1/settings")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isOk());

        // Create User 2
        String user2Token = createAndLoginUser("owner2@gymos.com", "GymOS@12345", "Diana", "Prince");

        // User 2 sets gym name to Gym Beta
        SettingsDTO dto2 = new SettingsDTO();
        SettingsDTO.GymProfileDTO profile2 = new SettingsDTO.GymProfileDTO();
        profile2.setGymName("Gym Beta");
        dto2.setGymProfile(profile2);

        mockMvc.perform(put("/api/v1/settings")
                        .header("Authorization", "Bearer " + user2Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isOk());

        // Verify User 1 gets Gym Alpha
        mockMvc.perform(get("/api/v1/settings")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.gymProfile.gymName", is("Gym Alpha")));

        // Verify User 2 gets Gym Beta
        mockMvc.perform(get("/api/v1/settings")
                        .header("Authorization", "Bearer " + user2Token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.gymProfile.gymName", is("Gym Beta")));
    }

    @Test
    @DisplayName("13. Both /api/v1/settings and /api/settings work seamlessly")
    void testBothUrlVersions() throws Exception {
        SettingsDTO dto = new SettingsDTO();
        SettingsDTO.AppearanceDTO appearance = new SettingsDTO.AppearanceDTO();
        appearance.setTheme("dark");
        dto.setAppearance(appearance);

        mockMvc.perform(put("/api/settings")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.settings.appearance.theme", is("dark")));

        mockMvc.perform(get("/api/settings")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.appearance.theme", is("dark")));
    }
}
