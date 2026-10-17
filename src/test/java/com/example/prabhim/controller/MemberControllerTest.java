package com.example.prabhim.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.prabhim.dto.LoginRequest;
import com.example.prabhim.dto.RegisterRequest;
import com.example.prabhim.dto.member.MemberCreateRequest;
import com.example.prabhim.dto.member.MemberRenewalRequest;
import com.example.prabhim.dto.member.QuickCheckInRequest;
import com.example.prabhim.entity.Member;
import com.example.prabhim.entity.User;
import com.example.prabhim.entity.enums.Gender;
import com.example.prabhim.entity.enums.MemberStatus;
import com.example.prabhim.repository.AttendanceRepository;
import com.example.prabhim.repository.MemberActivityLogRepository;
import com.example.prabhim.repository.MemberRepository;
import com.example.prabhim.repository.MembershipRepository;
import com.example.prabhim.repository.PaymentRepository;
import com.example.prabhim.repository.SessionRepository;
import com.example.prabhim.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class MemberControllerTest {

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
    @DisplayName("1. Add Member (POST /api/v1/members)")
    void testAddMember() throws Exception {
        MemberCreateRequest createReq = new MemberCreateRequest();
        createReq.setFirstName("Sophia");
        createReq.setLastName("Chen");
        createReq.setEmail("sophia.c@example.com");
        createReq.setPhone("+1 (555) 234-8901");
        createReq.setGender(Gender.FEMALE);
        createReq.setPlanName("12-Month All Access");
        createReq.setPlanDurationMonths(12);
        createReq.setPlanPrice(new BigDecimal("1200.00"));
        createReq.setStatus(MemberStatus.ACTIVE);

        mockMvc.perform(post("/api/v1/members")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", notNullValue()))
                .andExpect(jsonPath("$.data.memberCode", containsString("PF-")))
                .andExpect(jsonPath("$.data.fullName", is("Sophia Chen")))
                .andExpect(jsonPath("$.data.email", is("sophia.c@example.com")))
                .andExpect(jsonPath("$.data.currentPlan.planName", is("12-Month All Access")));
    }

    @Test
    @DisplayName("2. List All Members (GET /api/v1/members)")
    void testListAllMembers() throws Exception {
        // Create 2 members
        MemberCreateRequest req1 = new MemberCreateRequest();
        req1.setFirstName("Alex");
        req1.setLastName("Mercer");
        req1.setEmail("alex@example.com");
        req1.setPhone("+1 (555) 111-2222");
        req1.setStatus(MemberStatus.ACTIVE);

        MemberCreateRequest req2 = new MemberCreateRequest();
        req2.setFirstName("Emma");
        req2.setLastName("Watson");
        req2.setEmail("emma@example.com");
        req2.setPhone("+1 (555) 333-4444");
        req2.setStatus(MemberStatus.ACTIVE);

        mockMvc.perform(post("/api/v1/members")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/members")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/members")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.content", hasSize(2)));
    }

    @Test
    @DisplayName("3. Get One Member Using ID (GET /api/v1/members/{id})")
    void testGetMemberById() throws Exception {
        MemberCreateRequest createReq = new MemberCreateRequest();
        createReq.setFirstName("James");
        createReq.setLastName("Bond");
        createReq.setEmail("james.bond@mi6.gov.uk");
        createReq.setPhone("+44 7700 900007");
        createReq.setStatus(MemberStatus.ACTIVE);

        MvcResult result = mockMvc.perform(post("/api/v1/members")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        String memberId = json.get("data").get("id").asText();

        mockMvc.perform(get("/api/v1/members/" + memberId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(memberId)))
                .andExpect(jsonPath("$.data.fullName", is("James Bond")))
                .andExpect(jsonPath("$.data.email", is("james.bond@mi6.gov.uk")));
    }

    @Test
    @DisplayName("4. Edit Member & Delete Member (PUT/PATCH/DELETE /api/v1/members/{id})")
    void testEditAndDeleteMember() throws Exception {
        MemberCreateRequest createReq = new MemberCreateRequest();
        createReq.setFirstName("Bruce");
        createReq.setLastName("Wayne");
        createReq.setEmail("bruce@wayne.com");
        createReq.setPhone("+1 (555) 777-8888");
        createReq.setStatus(MemberStatus.ACTIVE);

        MvcResult result = mockMvc.perform(post("/api/v1/members")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        String memberId = json.get("data").get("id").asText();

        // 4a. Edit Member via PUT
        com.example.prabhim.dto.member.MemberUpdateRequest editReq = new com.example.prabhim.dto.member.MemberUpdateRequest();
        editReq.setFullName("Bruce Wayne Updated");
        editReq.setEmail("bruce.wayne@wayne.com");
        editReq.setPhone("+1 (555) 777-9999");
        editReq.setStatus(MemberStatus.ACTIVE);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/v1/members/" + memberId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.fullName", is("Bruce Wayne Updated")))
                .andExpect(jsonPath("$.data.email", is("bruce.wayne@wayne.com")));

        // 4b. Patch Member via PATCH
        com.example.prabhim.dto.member.MemberPatchRequest patchReq = new com.example.prabhim.dto.member.MemberPatchRequest();
        patchReq.setStatus(MemberStatus.EXPIRED);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/v1/members/" + memberId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.status", is("EXPIRED")));

        // 4c. Delete Member via DELETE
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/v1/members/" + memberId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        // Verify 404 after deletion
        mockMvc.perform(get("/api/v1/members/" + memberId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("5. Assign Trainer (PUT /api/v1/members/{id}/assign-trainer)")
    void testAssignTrainer() throws Exception {
        MemberCreateRequest createReq = new MemberCreateRequest();
        createReq.setFirstName("Clark");
        createReq.setLastName("Kent");
        createReq.setEmail("clark@dailyplanet.com");
        createReq.setStatus(MemberStatus.ACTIVE);

        MvcResult res = mockMvc.perform(post("/api/v1/members")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn();

        String memberId = objectMapper.readTree(res.getResponse().getContentAsString()).get("data").get("id").asText();

        // Get admin user ID as trainer
        User admin = userRepository.findByEmail("admin@gymos.com").orElseThrow();

        com.example.prabhim.dto.member.AssignTrainerRequest assignReq =
                new com.example.prabhim.dto.member.AssignTrainerRequest(admin.getId());

        mockMvc.perform(put("/api/v1/members/" + memberId + "/assign-trainer")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.assignedTrainer.id", is(admin.getId().toString())))
                .andExpect(jsonPath("$.data.assignedTrainerName", is("Marcus Vance")));
    }

    @Test
    @DisplayName("6. Renew Membership (POST /api/v1/members/{id}/renew)")
    void testRenewMembership() throws Exception {
        MemberCreateRequest createReq = new MemberCreateRequest();
        createReq.setFirstName("Diana");
        createReq.setLastName("Prince");
        createReq.setEmail("diana@themyscira.gov");
        createReq.setPlanName("Monthly Basic Pass");
        createReq.setPlanDurationMonths(1);
        createReq.setPlanPrice(new BigDecimal("1500.00"));
        createReq.setStatus(MemberStatus.ACTIVE);

        MvcResult res = mockMvc.perform(post("/api/v1/members")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn();

        String memberId = objectMapper.readTree(res.getResponse().getContentAsString()).get("data").get("id").asText();

        // Renew membership via POST /{id}/renew
        MemberRenewalRequest renewReq = new MemberRenewalRequest();
        renewReq.setPlanName("Quarterly Standard Pass");
        renewReq.setPlanType("STANDARD");
        renewReq.setDurationMonths(3);
        renewReq.setPrice(new BigDecimal("4000.00"));
        renewReq.setPaymentMethod("UPI");

        mockMvc.perform(post("/api/v1/members/" + memberId + "/renew")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(renewReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.planName", is("Quarterly Standard Pass")))
                .andExpect(jsonPath("$.data.status", is("ACTIVE")));
    }

    @Test
    @DisplayName("7. Member Dashboard Stats (GET /api/v1/members/stats)")
    void testGetMemberStats() throws Exception {
        MemberCreateRequest createReq = new MemberCreateRequest();
        createReq.setFirstName("Barry");
        createReq.setLastName("Allen");
        createReq.setEmail("barry@starlabs.com");
        createReq.setStatus(MemberStatus.ACTIVE);

        mockMvc.perform(post("/api/v1/members")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/members/stats")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.totalRecords", greaterThanOrEqualTo(1)));
    }

    @Test
    @DisplayName("8. 360 Member Profile (GET /api/v1/members/{id}/profile)")
    void testGetMemberProfile() throws Exception {
        MemberCreateRequest createReq = new MemberCreateRequest();
        createReq.setFirstName("Arthur");
        createReq.setLastName("Curry");
        createReq.setEmail("arthur@atlantis.com");
        createReq.setStatus(MemberStatus.ACTIVE);

        MvcResult res = mockMvc.perform(post("/api/v1/members")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn();

        String memberId = objectMapper.readTree(res.getResponse().getContentAsString()).get("data").get("id").asText();

        mockMvc.perform(get("/api/v1/members/" + memberId + "/profile")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.member.id", is(memberId)))
                .andExpect(jsonPath("$.data.attendanceSummary", notNullValue()));
    }

    @Test
    @DisplayName("9. Quick Check-In (POST /api/v1/members/{id}/check-in)")
    void testQuickCheckIn() throws Exception {
        MemberCreateRequest createReq = new MemberCreateRequest();
        createReq.setFirstName("Victor");
        createReq.setLastName("Stone");
        createReq.setEmail("victor@star.org");
        createReq.setStatus(MemberStatus.ACTIVE);

        MvcResult res = mockMvc.perform(post("/api/v1/members")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn();

        String memberId = objectMapper.readTree(res.getResponse().getContentAsString()).get("data").get("id").asText();

        QuickCheckInRequest checkInReq = new QuickCheckInRequest("Main Entrance Turnstile");

        mockMvc.perform(post("/api/v1/members/" + memberId + "/check-in")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkInReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.facility", is("Main Entrance Turnstile")));
    }

    @Test
    @DisplayName("10. Route Aliases & Frontend Compatibility (/api/v1/member, /export, /trainers)")
    void testRouteAliasesAndCompatibility() throws Exception {
        // Test singular route /api/v1/member
        mockMvc.perform(get("/api/v1/member")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        // Test trainers endpoint
        mockMvc.perform(get("/api/v1/members/trainers")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        // Test export CSV endpoint
        mockMvc.perform(get("/api/v1/members/export")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("text/csv")));
    }
}

