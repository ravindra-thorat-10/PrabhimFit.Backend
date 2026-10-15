package com.example.prabhim.controller;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.AfterEach;
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
import com.example.prabhim.dto.lead.DiscussionDto;
import com.example.prabhim.dto.lead.LeadCreateRequest;
import com.example.prabhim.dto.lead.LeadDiscussionRequest;
import com.example.prabhim.dto.lead.LeadUpdateRequest;
import com.example.prabhim.dto.lead.RemindersDto;
import com.example.prabhim.dto.lead.VisitorInfoDto;
import com.example.prabhim.entity.User;
import com.example.prabhim.repository.AttendanceRepository;
import com.example.prabhim.repository.LeadDiscussionRepository;
import com.example.prabhim.repository.LeadRepository;
import com.example.prabhim.repository.MemberActivityLogRepository;
import com.example.prabhim.repository.MemberRepository;
import com.example.prabhim.repository.MembershipPlanRepository;
import com.example.prabhim.repository.MembershipRepository;
import com.example.prabhim.repository.PaymentRepository;
import com.example.prabhim.repository.SessionRepository;
import com.example.prabhim.repository.TrainerRepository;
import com.example.prabhim.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class LeadControllerTest {

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
    private LeadDiscussionRepository leadDiscussionRepository;

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

    @Autowired
    private TrainerRepository trainerRepository;

    private String tokenAccountA;
    private String tokenAccountB;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        leadDiscussionRepository.deleteAll();
        leadRepository.deleteAll();
        attendanceRepository.deleteAll();
        membershipRepository.deleteAll();
        paymentRepository.deleteAll();
        memberActivityLogRepository.deleteAll();
        memberRepository.deleteAll();
        membershipPlanRepository.deleteAll();
        trainerRepository.deleteAll();
        sessionRepository.deleteAll();
        userRepository.deleteAll();

        // Register Account A
        tokenAccountA = registerAndLogin("admin_a@gymos.com", "Admin@12345", "Alice", "Admin");

        // Register Account B
        tokenAccountB = registerAndLogin("admin_b@gymos.com", "Admin@12345", "Bob", "Admin");
    }

    @AfterEach
    void tearDown() {
        leadDiscussionRepository.deleteAll();
        leadRepository.deleteAll();
        userRepository.deleteAll();
    }

    private String registerAndLogin(String email, String password, String firstName, String lastName) throws Exception {
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

    private LeadCreateRequest buildSampleLeadRequest() {
        LeadCreateRequest req = new LeadCreateRequest();
        req.setName("Amit Sharma");
        req.setPhone("9876543210");
        req.setEmail("amit.sharma@gmail.com");
        req.setGender("Male");
        req.setAge(27);
        req.setDob("1999-05-12");
        req.setProfession("Software Engineer");
        req.setAddress("Kalyani Nagar, Pune");
        req.setEmergencyContact("9876543200");
        req.setHeight("5 ft 9 in");
        req.setWeight("74 kg");
        req.setDietPreference("Non-Vegetarian");
        req.setWorkoutTimePreference("Evening (6:00 PM - 8:00 PM)");
        req.setPaymentModePreference("UPI");
        req.setStatus("NEW_INQUIRY");
        req.setSource("Instagram");
        req.setFitnessGoal("Muscle Gain & Strength");
        req.setTargetPlan("Gym Membership");
        req.setQuotedAmount(new BigDecimal("4500.00"));
        req.setDepositPaid(new BigDecimal("1000.00"));
        req.setFollowUpDate("2026-10-15");
        req.setFollowUpTime("05:30 PM");
        req.setExpectedJoiningDate("2026-10-20");
        req.setPriority("HIGH");
        req.setAssignedStaff("Marcus Trainer");
        req.setNotes("First walk-in visit inquiry.");

        VisitorInfoDto vi = new VisitorInfoDto();
        vi.setVisitDate("2026-10-14");
        vi.setTrialSlot("05:30 PM");
        vi.setTrialCompleted(false);
        vi.setInterestedInPT(true);
        vi.setHealthConditions("None");
        req.setVisitorInfo(vi);

        RemindersDto rem = new RemindersDto();
        rem.setCallReminder(true);
        rem.setCallTime("05:30 PM");
        rem.setMessageReminder(true);
        rem.setPaymentReminder(false);
        req.setReminders(rem);

        DiscussionDto disc = new DiscussionDto();
        disc.setChannel("Walk-in Visit");
        disc.setAuthor("Admin Desk");
        disc.setNotes("Initial inquiry about evening slots.");
        disc.setNextAction("Call scheduled");
        disc.setExpectedJoinDate("2026-10-20");
        req.setInitialDiscussion(disc);

        return req;
    }

    @Test
    @DisplayName("1. Create Lead with Valid Data (POST /api/v1/leads)")
    void testCreateLeadValid() throws Exception {
        LeadCreateRequest req = buildSampleLeadRequest();

        mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", "Bearer " + tokenAccountA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", notNullValue()))
                .andExpect(jsonPath("$.data.name", is("Amit Sharma")))
                .andExpect(jsonPath("$.data.phone", is("9876543210")))
                .andExpect(jsonPath("$.data.quotedAmount", is(4500.00)))
                .andExpect(jsonPath("$.data.depositPaid", is(1000.00)))
                .andExpect(jsonPath("$.data.balanceDue", is(3500.00)))
                .andExpect(jsonPath("$.data.status", is("NEW_INQUIRY")))
                .andExpect(jsonPath("$.data.source", is("Instagram")))
                .andExpect(jsonPath("$.data.tenantId", is("admin_a@gymos.com")))
                .andExpect(jsonPath("$.data.creatorEmail", is("admin_a@gymos.com")))
                .andExpect(jsonPath("$.data.visitorInfo.interestedInPT", is(true)))
                .andExpect(jsonPath("$.data.discussions", hasSize(1)));
    }

    @Test
    @DisplayName("2. Reject Create Lead with Invalid Required Fields (Name or Phone blank)")
    void testCreateLeadInvalidRequiredFields() throws Exception {
        LeadCreateRequest req = new LeadCreateRequest();
        req.setName(""); // blank name
        req.setPhone(""); // blank phone

        mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", "Bearer " + tokenAccountA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    @DisplayName("3. Retrieve Lead List (GET /api/v1/leads) returns Flat Array")
    void testGetLeadListFlatArray() throws Exception {
        // Create 2 leads under Account A
        mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", "Bearer " + tokenAccountA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildSampleLeadRequest())))
                .andExpect(status().isCreated());

        LeadCreateRequest req2 = buildSampleLeadRequest();
        req2.setName("Priya Patil");
        req2.setPhone("9822044119");
        mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", "Bearer " + tokenAccountA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req2)))
                .andExpect(status().isCreated());

        // GET /api/v1/leads without page returns array
        mockMvc.perform(get("/api/v1/leads")
                        .header("Authorization", "Bearer " + tokenAccountA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    @DisplayName("4. Retrieve Lead by ID (GET /api/v1/leads/{id})")
    void testGetLeadById() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", "Bearer " + tokenAccountA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildSampleLeadRequest())))
                .andExpect(status().isCreated())
                .andReturn();

        String leadId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("data").get("id").asText();

        mockMvc.perform(get("/api/v1/leads/" + leadId)
                        .header("Authorization", "Bearer " + tokenAccountA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(leadId)))
                .andExpect(jsonPath("$.data.name", is("Amit Sharma")));
    }

    @Test
    @DisplayName("5. Update Lead & Verify Persistence (PUT /api/v1/leads/{id})")
    void testUpdateLead() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", "Bearer " + tokenAccountA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildSampleLeadRequest())))
                .andExpect(status().isCreated())
                .andReturn();

        String leadId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("data").get("id").asText();

        LeadUpdateRequest updateReq = new LeadUpdateRequest();
        updateReq.setName("Amit Sharma Updated");
        updateReq.setQuotedAmount(new BigDecimal("5000.00"));
        updateReq.setDepositPaid(new BigDecimal("2000.00"));
        updateReq.setStatus("CONTACTED");

        mockMvc.perform(put("/api/v1/leads/" + leadId)
                        .header("Authorization", "Bearer " + tokenAccountA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("Amit Sharma Updated")))
                .andExpect(jsonPath("$.data.quotedAmount", is(5000.00)))
                .andExpect(jsonPath("$.data.depositPaid", is(2000.00)))
                .andExpect(jsonPath("$.data.balanceDue", is(3000.00)))
                .andExpect(jsonPath("$.data.status", is("CONTACTED")));
    }

    @Test
    @DisplayName("6. Delete Lead & Verify Expected Behavior (DELETE /api/v1/leads/{id})")
    void testDeleteLead() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", "Bearer " + tokenAccountA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildSampleLeadRequest())))
                .andExpect(status().isCreated())
                .andReturn();

        String leadId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("data").get("id").asText();

        mockMvc.perform(delete("/api/v1/leads/" + leadId)
                        .header("Authorization", "Bearer " + tokenAccountA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        // Verify it is no longer found
        mockMvc.perform(get("/api/v1/leads/" + leadId)
                        .header("Authorization", "Bearer " + tokenAccountA))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("7. Verify Search, Filters, Pagination, and Sorting")
    void testSearchFilterPaginationSorting() throws Exception {
        // Create multiple leads with different properties
        LeadCreateRequest lead1 = buildSampleLeadRequest();
        lead1.setName("Rohan Kulkarni");
        lead1.setStatus("NEW_INQUIRY");
        lead1.setSource("Instagram");
        lead1.setPriority("HIGH");
        lead1.setGender("Male");
        mockMvc.perform(post("/api/v1/leads").header("Authorization", "Bearer " + tokenAccountA)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(lead1))).andExpect(status().isCreated());

        LeadCreateRequest lead2 = buildSampleLeadRequest();
        lead2.setName("Pooja Mehta");
        lead2.setStatus("CONTACTED");
        lead2.setSource("Walk-in");
        lead2.setPriority("MEDIUM");
        lead2.setGender("Female");
        mockMvc.perform(post("/api/v1/leads").header("Authorization", "Bearer " + tokenAccountA)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(lead2))).andExpect(status().isCreated());

        LeadCreateRequest lead3 = buildSampleLeadRequest();
        lead3.setName("Vikram Joshi");
        lead3.setStatus("CONVERTED");
        lead3.setSource("Google");
        lead3.setPriority("LOW");
        lead3.setGender("Male");
        mockMvc.perform(post("/api/v1/leads").header("Authorization", "Bearer " + tokenAccountA)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(lead3))).andExpect(status().isCreated());

        // 7a. Search by name
        mockMvc.perform(get("/api/v1/leads").param("search", "Pooja")
                        .header("Authorization", "Bearer " + tokenAccountA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", is("Pooja Mehta")));

        // 7b. Filter by status (CONTACTED)
        mockMvc.perform(get("/api/v1/leads").param("status", "CONTACTED")
                        .header("Authorization", "Bearer " + tokenAccountA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", is("Pooja Mehta")));

        // 7c. Filter by source (Google)
        mockMvc.perform(get("/api/v1/leads").param("source", "Google")
                        .header("Authorization", "Bearer " + tokenAccountA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", is("Vikram Joshi")));

        // 7d. Pagination (page=0, size=2)
        mockMvc.perform(get("/api/v1/leads")
                        .param("page", "0")
                        .param("size", "2")
                        .header("Authorization", "Bearer " + tokenAccountA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content", hasSize(2)))
                .andExpect(jsonPath("$.data.totalElements", is(3)))
                .andExpect(jsonPath("$.data.totalPages", is(2)));
    }

    @Test
    @DisplayName("8. Verify Dropdowns, Discussion Log, and Stats Endpoints")
    void testDropdownsDiscussionsStats() throws Exception {
        // 8a. Options endpoint
        mockMvc.perform(get("/api/v1/leads/options")
                        .header("Authorization", "Bearer " + tokenAccountA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.statuses", not(empty())))
                .andExpect(jsonPath("$.data.sources", not(empty())));

        // Create a lead
        MvcResult createResult = mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", "Bearer " + tokenAccountA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildSampleLeadRequest())))
                .andExpect(status().isCreated())
                .andReturn();
        String leadId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("data").get("id").asText();

        // 8b. Add discussion log
        LeadDiscussionRequest discReq = new LeadDiscussionRequest();
        discReq.setChannel("Phone Call");
        discReq.setAuthor("Manager");
        discReq.setNotes("Discussed annual plan discount.");
        discReq.setNextAction("Follow up on Friday");
        discReq.setNewStatus("FOLLOWUP_SCHEDULED");

        mockMvc.perform(post("/api/v1/leads/" + leadId + "/discussions")
                        .header("Authorization", "Bearer " + tokenAccountA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status", is("FOLLOWUP_SCHEDULED")))
                .andExpect(jsonPath("$.data.discussions", hasSize(2)));

        // 8c. Convert to member
        mockMvc.perform(post("/api/v1/leads/" + leadId + "/convert")
                        .header("Authorization", "Bearer " + tokenAccountA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status", is("CONVERTED")))
                .andExpect(jsonPath("$.data.nextAction", is("Enrolled Member")));

        // 8d. Stats endpoint
        mockMvc.perform(get("/api/v1/leads/stats")
                        .header("Authorization", "Bearer " + tokenAccountA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalLeads", is(1)))
                .andExpect(jsonPath("$.data.convertedLeads", is(1)));
    }

    @Test
    @DisplayName("9. Reject Unauthenticated Requests (401 Unauthorized)")
    void testUnauthenticatedRequestsRejected() throws Exception {
        mockMvc.perform(get("/api/v1/leads"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success", is(false)));

        mockMvc.perform(post("/api/v1/leads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildSampleLeadRequest())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("10-12. Strict Multi-Account Isolation: Account B cannot view, update, or delete Account A's lead")
    void testStrictMultiAccountIsolation() throws Exception {
        // Account A creates a lead
        MvcResult createResultA = mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", "Bearer " + tokenAccountA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildSampleLeadRequest())))
                .andExpect(status().isCreated())
                .andReturn();
        String leadIdA = objectMapper.readTree(createResultA.getResponse().getContentAsString()).get("data").get("id").asText();

        // Account B creates their own lead
        LeadCreateRequest reqB = buildSampleLeadRequest();
        reqB.setName("Bob Prospect");
        reqB.setEmail("bob.prospect@gmail.com");
        MvcResult createResultB = mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", "Bearer " + tokenAccountB)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqB)))
                .andExpect(status().isCreated())
                .andReturn();
        String leadIdB = objectMapper.readTree(createResultB.getResponse().getContentAsString()).get("data").get("id").asText();

        // 1. Account A list only contains Account A's lead (count = 1)
        mockMvc.perform(get("/api/v1/leads")
                        .header("Authorization", "Bearer " + tokenAccountA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(leadIdA)))
                .andExpect(jsonPath("$.data[0].tenantId", is("admin_a@gymos.com")));

        // 2. Account B list only contains Account B's lead (count = 1)
        mockMvc.perform(get("/api/v1/leads")
                        .header("Authorization", "Bearer " + tokenAccountB))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(leadIdB)))
                .andExpect(jsonPath("$.data[0].tenantId", is("admin_b@gymos.com")));

        // 3. Account B attempts to GET Account A's lead by ID -> 403 Forbidden
        mockMvc.perform(get("/api/v1/leads/" + leadIdA)
                        .header("Authorization", "Bearer " + tokenAccountB))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success", is(false)));

        // 4. Account B attempts to UPDATE Account A's lead -> 403 Forbidden
        LeadUpdateRequest updateReq = new LeadUpdateRequest();
        updateReq.setName("Hacked By Account B");
        mockMvc.perform(put("/api/v1/leads/" + leadIdA)
                        .header("Authorization", "Bearer " + tokenAccountB)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success", is(false)));

        // 5. Account B attempts to DELETE Account A's lead -> 403 Forbidden
        mockMvc.perform(delete("/api/v1/leads/" + leadIdA)
                        .header("Authorization", "Bearer " + tokenAccountB))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success", is(false)));

        // 6. Verify Account A's lead is still intact and not tampered with
        mockMvc.perform(get("/api/v1/leads/" + leadIdA)
                        .header("Authorization", "Bearer " + tokenAccountA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is("Amit Sharma")));
    }
}
