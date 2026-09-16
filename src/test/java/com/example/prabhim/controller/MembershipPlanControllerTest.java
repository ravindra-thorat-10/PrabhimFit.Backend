package com.example.prabhim.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import com.example.prabhim.dto.member.MemberCreateRequest;
import com.example.prabhim.dto.plan.MembershipPlanCreateRequest;
import com.example.prabhim.dto.plan.MembershipPlanUpdateRequest;
import com.example.prabhim.entity.User;
import com.example.prabhim.entity.enums.Gender;
import com.example.prabhim.entity.enums.MemberStatus;
import com.example.prabhim.repository.AttendanceRepository;
import com.example.prabhim.repository.MemberActivityLogRepository;
import com.example.prabhim.repository.MemberRepository;
import com.example.prabhim.repository.MembershipPlanRepository;
import com.example.prabhim.repository.MembershipRepository;
import com.example.prabhim.repository.PaymentRepository;
import com.example.prabhim.repository.SessionRepository;
import com.example.prabhim.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class MembershipPlanControllerTest {

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
    private MemberRepository memberRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private MembershipPlanRepository membershipPlanRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MemberActivityLogRepository memberActivityLogRepository;

    private String authToken;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        attendanceRepository.deleteAll();
        membershipRepository.deleteAll();
        paymentRepository.deleteAll();
        memberActivityLogRepository.deleteAll();
        memberRepository.deleteAll();
        membershipPlanRepository.deleteAll();
        sessionRepository.deleteAll();
        userRepository.deleteAll();

        // Register and verify a user to get auth token
        RegisterRequest registerReq = new RegisterRequest();
        registerReq.setEmail("admin@gymos.com");
        registerReq.setPassword("Admin@12345");
        registerReq.setPasswordConfirm("Admin@12345");
        registerReq.setFirstName("Marcus");
        registerReq.setLastName("Vance");

        mockMvc.perform(post("/api/v1/auth/register/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isCreated());

        User user = userRepository.findByEmail("admin@gymos.com").orElseThrow();
        user.setEmailVerified(true);
        user.setStaff(true);
        user.setDesignation("Director");
        userRepository.save(user);

        LoginRequest loginReq = new LoginRequest("admin@gymos.com", "Admin@12345");
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode json = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        this.authToken = json.get("data").get("access").asText();
    }

    @Test
    @DisplayName("1. Get All Membership Plans - Auto seeds default 4 catalog plans")
    void testGetAllPlansAutoSeeded() throws Exception {
        mockMvc.perform(get("/api/v1/membership-plans")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(4)))
                .andExpect(jsonPath("$.data[0].name", is("Basic Fitness")))
                .andExpect(jsonPath("$.data[0].price", is(1500.00)))
                .andExpect(jsonPath("$.data[0].durationLabel", is("1 Month")))
                .andExpect(jsonPath("$.data[1].name", is("Standard Pro")))
                .andExpect(jsonPath("$.data[1].price", is(4000.00)))
                .andExpect(jsonPath("$.data[1].durationLabel", is("3 Months")))
                .andExpect(jsonPath("$.data[2].name", is("Premium Elite")))
                .andExpect(jsonPath("$.data[2].price", is(7000.00)))
                .andExpect(jsonPath("$.data[2].durationLabel", is("6 Months")))
                .andExpect(jsonPath("$.data[3].name", is("Annual VIP Athlete")))
                .andExpect(jsonPath("$.data[3].price", is(12000.00)))
                .andExpect(jsonPath("$.data[3].durationLabel", is("12 Months")));
    }

    @Test
    @DisplayName("2. Create New Membership Plan via Modal Payload")
    void testCreateMembershipPlan() throws Exception {
        MembershipPlanCreateRequest createReq = new MembershipPlanCreateRequest();
        createReq.setPlanName("Weekend Warrior");
        createReq.setDurationMonths(2);
        createReq.setPrice(new BigDecimal("2500.00"));
        createReq.setShortDescription("Weekend only access with pool and sauna privileges");
        createReq.setFeatures("Saturday & Sunday Gym Access, Swimming Pool, Sauna, Locker Access");
        createReq.setStatus("ACTIVE");

        MvcResult result = mockMvc.perform(post("/api/v1/membership-plans")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", notNullValue()))
                .andExpect(jsonPath("$.data.name", is("Weekend Warrior")))
                .andExpect(jsonPath("$.data.durationMonths", is(2)))
                .andExpect(jsonPath("$.data.durationLabel", is("2 Months")))
                .andExpect(jsonPath("$.data.price", is(2500.00)))
                .andExpect(jsonPath("$.data.features", hasSize(4)))
                .andExpect(jsonPath("$.data.activeSubscriptions", is(0)))
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        String planIdStr = json.get("data").get("id").asText();
        UUID planId = UUID.fromString(planIdStr);

        // Get by ID
        mockMvc.perform(get("/api/v1/membership-plans/" + planId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is("Weekend Warrior")));
    }

    @Test
    @DisplayName("3. Update & Delete Membership Plan")
    void testUpdateAndDeletePlan() throws Exception {
        MembershipPlanCreateRequest createReq = new MembershipPlanCreateRequest();
        createReq.setName("Trial Pass");
        createReq.setPrice(new BigDecimal("500.00"));
        createReq.setDurationMonths(1);
        createReq.setDescription("1 month trial");
        createReq.setFeaturesList(List.of("Floor Access", "Shower"));
        createReq.setStatus("ACTIVE");

        MvcResult createResult = mockMvc.perform(post("/api/v1/membership-plans")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn();

        String planIdStr = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("data").get("id").asText();
        UUID planId = UUID.fromString(planIdStr);

        // Update with PUT
        MembershipPlanUpdateRequest updateReq = new MembershipPlanUpdateRequest();
        updateReq.setPlanName("Trial Pass Ultra");
        updateReq.setPrice(new BigDecimal("600.00"));
        updateReq.setShortDescription("1 month trial with perks");
        updateReq.setFeatures("Floor Access, Shower, 1 Smoothie");

        mockMvc.perform(put("/api/v1/membership-plans/" + planId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is("Trial Pass Ultra")))
                .andExpect(jsonPath("$.data.price", is(600.00)))
                .andExpect(jsonPath("$.data.features", hasSize(3)));

        // Patch Status
        MembershipPlanUpdateRequest patchReq = new MembershipPlanUpdateRequest();
        patchReq.setStatus("INACTIVE");

        mockMvc.perform(patch("/api/v1/membership-plans/" + planId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status", is("INACTIVE")));

        // Delete Plan
        mockMvc.perform(delete("/api/v1/membership-plans/" + planId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        // Verify 404 after delete
        mockMvc.perform(get("/api/v1/membership-plans/" + planId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("4. Active Subscriptions count calculation for Membership Plans")
    void testActiveSubscriptionsCountCalculation() throws Exception {
        // Create 2 members with "Basic Fitness" plan
        MemberCreateRequest member1 = new MemberCreateRequest();
        member1.setFirstName("Alice");
        member1.setLastName("Smith");
        member1.setEmail("alice@test.com");
        member1.setPhone("+15551112222");
        member1.setGender(Gender.FEMALE);
        member1.setPlanName("Basic Fitness");
        member1.setStatus(MemberStatus.ACTIVE);

        mockMvc.perform(post("/api/v1/members/")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member1)))
                .andExpect(status().isCreated());

        MemberCreateRequest member2 = new MemberCreateRequest();
        member2.setFirstName("Bob");
        member2.setLastName("Jones");
        member2.setEmail("bob@test.com");
        member2.setPhone("+15552223333");
        member2.setGender(Gender.MALE);
        member2.setPlanName("Basic Fitness");
        member2.setStatus(MemberStatus.ACTIVE);

        mockMvc.perform(post("/api/v1/members/")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member2)))
                .andExpect(status().isCreated());

        // Get All Plans and verify "Basic Fitness" shows activeSubscriptions: 2
        mockMvc.perform(get("/api/v1/membership-plans")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name", is("Basic Fitness")))
                .andExpect(jsonPath("$.data[0].activeSubscriptions", is(2)));
    }
}
