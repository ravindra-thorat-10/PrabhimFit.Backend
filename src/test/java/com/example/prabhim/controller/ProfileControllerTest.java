package com.example.prabhim.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.prabhim.dto.LoginRequest;
import com.example.prabhim.dto.RegisterRequest;
import com.example.prabhim.dto.profile.AvatarUploadRequest;
import com.example.prabhim.dto.profile.ChangePasswordRequest;
import com.example.prabhim.dto.profile.ProfilePatchRequest;
import com.example.prabhim.dto.profile.ProfileUpdateRequest;
import com.example.prabhim.entity.User;
import com.example.prabhim.repository.AttendanceRepository;
import com.example.prabhim.repository.BodyMeasurementRepository;
import com.example.prabhim.repository.DietPlanRepository;
import com.example.prabhim.repository.LeadRepository;
import com.example.prabhim.repository.MemberActivityLogRepository;
import com.example.prabhim.repository.MemberRepository;
import com.example.prabhim.repository.MembershipRepository;
import com.example.prabhim.repository.PaymentRepository;
import com.example.prabhim.repository.PtSessionRepository;
import com.example.prabhim.repository.SessionRepository;
import com.example.prabhim.repository.UserRepository;
import com.example.prabhim.repository.WorkoutLogRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class ProfileControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private MemberActivityLogRepository memberActivityLogRepository;

    @Autowired
    private PtSessionRepository ptSessionRepository;

    @Autowired
    private WorkoutLogRepository workoutLogRepository;

    @Autowired
    private DietPlanRepository dietPlanRepository;

    @Autowired
    private BodyMeasurementRepository bodyMeasurementRepository;

    private String authToken;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        leadRepository.deleteAll();
        attendanceRepository.deleteAll();
        memberActivityLogRepository.deleteAll();
        ptSessionRepository.deleteAll();
        workoutLogRepository.deleteAll();
        dietPlanRepository.deleteAll();
        bodyMeasurementRepository.deleteAll();
        paymentRepository.deleteAll();
        membershipRepository.deleteAll();
        memberRepository.deleteAll();
        sessionRepository.deleteAll();
        userRepository.deleteAll();

        // Register and login admin
        RegisterRequest registerReq = new RegisterRequest();
        registerReq.setEmail("vishaljagdhane95@gmail.com");
        registerReq.setPassword("Admin@12345");
        registerReq.setPasswordConfirm("Admin@12345");
        registerReq.setFirstName("Vishal");
        registerReq.setLastName("Jagdhane");

        mockMvc.perform(post("/api/v1/auth/register/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isCreated());

        User user = userRepository.findByEmail("vishaljagdhane95@gmail.com").orElseThrow();
        user.setEmailVerified(true);
        user.setSuperuser(true);
        user.setStaff(true);
        user.setPhone("7887828848");
        user.setDesignation("Managing Director & Chief Administrator");
        user.setFacilityName("Vishal Fit Gym & Performance Club");
        user.setCity("Pune");
        user.setState("Maharashtra");
        user.setPincode("412207");
        user.setResidentialAddress("Flat 1002-A, Utsav Residency Phase 1, Awhalwadi Road");
        user.setSecondaryContactName("Ravindra Thorat");
        user.setSecondaryContactPhone("+91 98220 12345");
        user.setSecondaryContactRole("Operations Partner");
        user.setBio("Master Administrator and Gym Owner overseeing elite fitness programming, operational billing, trainer staff, and member lifecycle intelligence.");
        userRepository.save(user);

        LoginRequest loginReq = new LoginRequest("vishaljagdhane95@gmail.com", "Admin@12345");
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        authToken = "Bearer " + root.path("data").path("access").asText();
    }

    @Test
    @DisplayName("1. GET /api/v1/profile - Fetch Administrator Profile")
    void testGetProfile() throws Exception {
        mockMvc.perform(get("/api/v1/profile")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.fullName", is("Vishal Jagdhane")))
                .andExpect(jsonPath("$.data.email", is("vishaljagdhane95@gmail.com")))
                .andExpect(jsonPath("$.data.phone", is("7887828848")))
                .andExpect(jsonPath("$.data.designation", is("Managing Director & Chief Administrator")))
                .andExpect(jsonPath("$.data.roleBadge", is("SUPER ADMIN")))
                .andExpect(jsonPath("$.data.privilegeLevel", is("Full Root / Superadmin")))
                .andExpect(jsonPath("$.data.twoFactorSecurity", is("Enabled & Verified")))
                .andExpect(jsonPath("$.data.linkedFacility", is("Vishal Fit Gym & Performance Club")))
                .andExpect(jsonPath("$.data.residentialAddress", is("Flat 1002-A, Utsav Residency Phase 1, Awhalwadi Road")))
                .andExpect(jsonPath("$.data.city", is("Pune")))
                .andExpect(jsonPath("$.data.state", is("Maharashtra")))
                .andExpect(jsonPath("$.data.pincode", is("412207")))
                .andExpect(jsonPath("$.data.secondaryContactName", is("Ravindra Thorat")))
                .andExpect(jsonPath("$.data.secondaryContactPhone", is("+91 98220 12345")))
                .andExpect(jsonPath("$.data.secondaryContactRole", is("Operations Partner")))
                .andExpect(jsonPath("$.data.bio", is("Master Administrator and Gym Owner overseeing elite fitness programming, operational billing, trainer staff, and member lifecycle intelligence.")));
    }

    @Test
    @DisplayName("2. PUT /api/v1/profile - Full Update Administrator Profile")
    void testUpdateProfile() throws Exception {
        ProfileUpdateRequest req = new ProfileUpdateRequest();
        req.setFullName("Vishal S. Jagdhane");
        req.setEmail("vishaljagdhane95@gmail.com");
        req.setPhone("9988776655");
        req.setDesignation("Executive Founder & Managing Director");
        req.setResidentialAddress("Villa 404, Amanora Park Town");
        req.setCity("Pune");
        req.setState("Maharashtra");
        req.setPincode("411028");
        req.setSecondaryContactName("Suresh Jagdhane");
        req.setSecondaryContactPhone("+91 98765 00000");
        req.setSecondaryContactRole("Co-Founder");
        req.setBio("Pioneering the future of fitness management.");

        mockMvc.perform(put("/api/v1/profile")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.fullName", is("Vishal S. Jagdhane")))
                .andExpect(jsonPath("$.data.phone", is("9988776655")))
                .andExpect(jsonPath("$.data.designation", is("Executive Founder & Managing Director")))
                .andExpect(jsonPath("$.data.residentialAddress", is("Villa 404, Amanora Park Town")))
                .andExpect(jsonPath("$.data.pincode", is("411028")))
                .andExpect(jsonPath("$.data.secondaryContactName", is("Suresh Jagdhane")))
                .andExpect(jsonPath("$.data.bio", is("Pioneering the future of fitness management.")));
    }

    @Test
    @DisplayName("3. PATCH /api/v1/profile - Partial Update Profile")
    void testPatchProfile() throws Exception {
        ProfilePatchRequest patchReq = new ProfilePatchRequest();
        patchReq.setDesignation("Chief Executive Officer");
        patchReq.setBio("Updated bio statement.");

        mockMvc.perform(patch("/api/v1/profile")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.designation", is("Chief Executive Officer")))
                .andExpect(jsonPath("$.data.bio", is("Updated bio statement.")))
                .andExpect(jsonPath("$.data.fullName", is("Vishal Jagdhane"))); // Unchanged
    }

    @Test
    @DisplayName("4. POST /api/v1/profile/avatar - Update Profile Avatar")
    void testUpdateAvatar() throws Exception {
        AvatarUploadRequest req = new AvatarUploadRequest("/avatars/admin-preset-2.png");

        mockMvc.perform(post("/api/v1/profile/avatar")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.profileImage", is("/avatars/admin-preset-2.png")));
    }

    @Test
    @DisplayName("5. POST /api/v1/profile/change-password - Change Password")
    void testChangePassword() throws Exception {
        ChangePasswordRequest req = new ChangePasswordRequest();
        req.setCurrentPassword("Admin@12345");
        req.setNewPassword("NewSecurePass@2026");
        req.setConfirmPassword("NewSecurePass@2026");

        mockMvc.perform(post("/api/v1/profile/change-password")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    @DisplayName("6. GET /api/v1/profile/presets - Get Avatar Presets")
    void testGetAvatarPresets() throws Exception {
        mockMvc.perform(get("/api/v1/profile/presets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(6)));
    }
}
