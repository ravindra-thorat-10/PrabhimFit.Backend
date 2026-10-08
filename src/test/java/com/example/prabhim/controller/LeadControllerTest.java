package com.example.prabhim.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

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
import com.example.prabhim.dto.lead.LeadConvertToMemberRequest;
import com.example.prabhim.dto.lead.LeadCreateRequest;
import com.example.prabhim.dto.lead.LeadPatchRequest;
import com.example.prabhim.dto.lead.LeadUpdateRequest;
import com.example.prabhim.entity.Lead;
import com.example.prabhim.entity.User;
import com.example.prabhim.entity.enums.Gender;
import com.example.prabhim.entity.enums.LeadPaymentStatus;
import com.example.prabhim.entity.enums.LeadPriority;
import com.example.prabhim.entity.enums.LeadSource;
import com.example.prabhim.entity.enums.LeadStatus;
import com.example.prabhim.entity.enums.PaymentModePreference;
import com.example.prabhim.repository.LeadRepository;
import com.example.prabhim.repository.MemberRepository;
import com.example.prabhim.repository.MembershipRepository;
import com.example.prabhim.repository.PaymentRepository;
import com.example.prabhim.repository.SessionRepository;
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
    private MemberRepository memberRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private com.example.prabhim.repository.AttendanceRepository attendanceRepository;

    @Autowired
    private com.example.prabhim.repository.MemberActivityLogRepository memberActivityLogRepository;

    @Autowired
    private com.example.prabhim.repository.PtSessionRepository ptSessionRepository;

    @Autowired
    private com.example.prabhim.repository.WorkoutLogRepository workoutLogRepository;

    @Autowired
    private com.example.prabhim.repository.DietPlanRepository dietPlanRepository;

    @Autowired
    private com.example.prabhim.repository.BodyMeasurementRepository bodyMeasurementRepository;

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

        // Register and login user
        RegisterRequest registerReq = new RegisterRequest();
        registerReq.setEmail("admin@gymcrm.com");
        registerReq.setPassword("Admin@12345");
        registerReq.setPasswordConfirm("Admin@12345");
        registerReq.setFirstName("Gym");
        registerReq.setLastName("Admin");

        mockMvc.perform(post("/api/v1/auth/register/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isCreated());

        User user = userRepository.findByEmail("admin@gymcrm.com").orElseThrow();
        user.setEmailVerified(true);
        user.setStaff(true);
        userRepository.save(user);

        LoginRequest loginReq = new LoginRequest("admin@gymcrm.com", "Admin@12345");

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        authToken = "Bearer " + root.path("data").path("access").asText();
    }

    private LeadCreateRequest buildSampleLeadRequest() {
        LeadCreateRequest req = new LeadCreateRequest();
        // Section 1
        req.setFullName("Rahul Sharma");
        req.setPhone("+91 98765 43210");
        req.setGender(Gender.MALE);
        req.setAge(26);
        req.setProfession("Software Engineer");
        req.setResidentialArea("Kalyani Nagar, Pune");
        req.setEmail("rahul.sharma@example.com");
        req.setEmergencyContact("Suresh Sharma (+91 98220 11223)");

        // Section 2
        req.setFitnessGoal("Muscle Gain & Strength");
        req.setPhysicalProfileNotes("Intermediate / 75kg");

        // Section 3
        req.setPlanName("Monthly Basic Pass");
        req.setStandardPrice(new BigDecimal("1500.00"));
        req.setQuotedFee(new BigDecimal("1500.00"));
        req.setAdvanceToken(new BigDecimal("0.00"));
        req.setPaymentModePreference(PaymentModePreference.UPI);

        // Section 4
        req.setFollowUpDate(LocalDate.now());
        req.setFollowUpTime("05:00 PM");
        req.setExpectedJoiningDate(LocalDate.now().plusDays(3));
        req.setInquirySource(LeadSource.WALK_IN);
        req.setPriority(LeadPriority.HIGH);
        req.setStatus(LeadStatus.NEW);

        // Section 5
        req.setDiscussionNotes("First inquiry at reception desk.");
        req.setWhatsappReminder(true);
        req.setPhoneCallTask(true);
        req.setPaymentDuesReminder(false);

        return req;
    }

    @Test
    @DisplayName("1. POST /api/v1/leads - Register New Lead / Walk-in Visitor")
    void testCreateLead() throws Exception {
        LeadCreateRequest req = buildSampleLeadRequest();

        mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.leadCode", containsString("LD-")))
                .andExpect(jsonPath("$.data.fullName", is("Rahul Sharma")))
                .andExpect(jsonPath("$.data.phone", is("+91 98765 43210")))
                .andExpect(jsonPath("$.data.gender", is("MALE")))
                .andExpect(jsonPath("$.data.age", is(26)))
                .andExpect(jsonPath("$.data.profession", is("Software Engineer")))
                .andExpect(jsonPath("$.data.residentialArea", is("Kalyani Nagar, Pune")))
                .andExpect(jsonPath("$.data.fitnessGoal", is("Muscle Gain & Strength")))
                .andExpect(jsonPath("$.data.planName", is("Monthly Basic Pass")))
                .andExpect(jsonPath("$.data.quotedFee", is(1500.00)))
                .andExpect(jsonPath("$.data.advanceToken", is(0.00)))
                .andExpect(jsonPath("$.data.pendingPlanFee", is(1500.00)))
                .andExpect(jsonPath("$.data.paymentStatus", is("PENDING")))
                .andExpect(jsonPath("$.data.paymentModePreference", is("UPI")))
                .andExpect(jsonPath("$.data.followUpTime", is("05:00 PM")))
                .andExpect(jsonPath("$.data.inquirySource", is("WALK_IN")))
                .andExpect(jsonPath("$.data.priority", is("HIGH")))
                .andExpect(jsonPath("$.data.discussionNotes", is("First inquiry at reception desk.")))
                .andExpect(jsonPath("$.data.whatsappReminder", is(true)))
                .andExpect(jsonPath("$.data.phoneCallTask", is(true)))
                .andExpect(jsonPath("$.data.paymentDuesReminder", is(false)));
    }

    @Test
    @DisplayName("2. GET /api/v1/leads - List All Leads with Search & Filters")
    void testGetAllLeads() throws Exception {
        LeadCreateRequest req1 = buildSampleLeadRequest();
        req1.setFullName("Amit Kumar");
        req1.setPhone("+91 9988776655");
        req1.setPriority(LeadPriority.HOT);
        req1.setInquirySource(LeadSource.WALK_IN);

        LeadCreateRequest req2 = buildSampleLeadRequest();
        req2.setFullName("Priya Patil");
        req2.setPhone("+91 9123456780");
        req2.setGender(Gender.FEMALE);
        req2.setPriority(LeadPriority.LOW);
        req2.setInquirySource(LeadSource.WEBSITE);

        mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req2)))
                .andExpect(status().isCreated());

        // Get All
        mockMvc.perform(get("/api/v1/leads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.totalElements", is(2)));

        // Filter by gender
        mockMvc.perform(get("/api/v1/leads?gender=FEMALE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements", is(1)))
                .andExpect(jsonPath("$.data.content[0].fullName", is("Priya Patil")));

        // Search by name
        mockMvc.perform(get("/api/v1/leads?search=Amit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements", is(1)))
                .andExpect(jsonPath("$.data.content[0].fullName", is("Amit Kumar")));
    }

    @Test
    @DisplayName("3. GET /api/v1/leads/stats - Pipeline KPI Metric Summary")
    void testGetLeadStats() throws Exception {
        LeadCreateRequest req = buildSampleLeadRequest();
        req.setInquirySource(LeadSource.WALK_IN);
        req.setQuotedFee(new BigDecimal("2000"));
        req.setAdvanceToken(new BigDecimal("500"));

        mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/leads/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.totalLeads", is(1)))
                .andExpect(jsonPath("$.data.todaysDue", is(1)))
                .andExpect(jsonPath("$.data.paymentDue", is(1)))
                .andExpect(jsonPath("$.data.visitors", is(1)));
    }

    @Test
    @DisplayName("4. GET /api/v1/leads/{id} - Get Lead by ID")
    void testGetLeadById() throws Exception {
        LeadCreateRequest req = buildSampleLeadRequest();

        MvcResult createResult = mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn();

        String leadId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .path("data").path("id").asText();

        mockMvc.perform(get("/api/v1/leads/" + leadId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(leadId)))
                .andExpect(jsonPath("$.data.fullName", is("Rahul Sharma")));
    }

    @Test
    @DisplayName("5. PUT /api/v1/leads/{id} - Full Update Lead")
    void testUpdateLead() throws Exception {
        LeadCreateRequest createReq = buildSampleLeadRequest();

        MvcResult createResult = mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn();

        String leadId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .path("data").path("id").asText();

        LeadUpdateRequest updateReq = new LeadUpdateRequest();
        updateReq.setFullName("Rahul S. Sharma");
        updateReq.setPhone("+91 98765 43210");
        updateReq.setGender(Gender.MALE);
        updateReq.setAge(27);
        updateReq.setProfession("Senior Software Engineer");
        updateReq.setResidentialArea("Koregaon Park, Pune");
        updateReq.setEmail("rahul.new@example.com");
        updateReq.setFitnessGoal("Bodybuilding");
        updateReq.setPlanName("Annual Elite");
        updateReq.setQuotedFee(new BigDecimal("12000.00"));
        updateReq.setAdvanceToken(new BigDecimal("2000.00"));
        updateReq.setFollowUpDate(LocalDate.now().plusDays(2));
        updateReq.setFollowUpTime("06:30 PM");
        updateReq.setExpectedJoiningDate(LocalDate.now().plusDays(5));
        updateReq.setPriority(LeadPriority.HOT);
        updateReq.setStatus(LeadStatus.NEGOTIATION);
        updateReq.setDiscussionNotes("Offered 10% annual discount");

        mockMvc.perform(put("/api/v1/leads/" + leadId)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fullName", is("Rahul S. Sharma")))
                .andExpect(jsonPath("$.data.age", is(27)))
                .andExpect(jsonPath("$.data.pendingPlanFee", is(10000.00)))
                .andExpect(jsonPath("$.data.paymentStatus", is("PARTIAL")))
                .andExpect(jsonPath("$.data.status", is("NEGOTIATION")));
    }

    @Test
    @DisplayName("6. PATCH /api/v1/leads/{id} - Partial Update Lead")
    void testPatchLead() throws Exception {
        LeadCreateRequest createReq = buildSampleLeadRequest();

        MvcResult createResult = mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn();

        String leadId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .path("data").path("id").asText();

        LeadPatchRequest patchReq = new LeadPatchRequest();
        patchReq.setStatus(LeadStatus.TRIAL_SCHEDULED);
        patchReq.setPriority(LeadPriority.HOT);
        patchReq.setDiscussionNotes("Trial session booked for Saturday 10 AM");

        mockMvc.perform(patch("/api/v1/leads/" + leadId)
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status", is("TRIAL_SCHEDULED")))
                .andExpect(jsonPath("$.data.priority", is("HOT")))
                .andExpect(jsonPath("$.data.discussionNotes", is("Trial session booked for Saturday 10 AM")))
                .andExpect(jsonPath("$.data.fullName", is("Rahul Sharma"))); // Unchanged
    }

    @Test
    @DisplayName("7. DELETE /api/v1/leads/{id} - Delete Lead")
    void testDeleteLead() throws Exception {
        LeadCreateRequest createReq = buildSampleLeadRequest();

        MvcResult createResult = mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn();

        String leadId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .path("data").path("id").asText();

        mockMvc.perform(delete("/api/v1/leads/" + leadId)
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        mockMvc.perform(get("/api/v1/leads/" + leadId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("8. POST /api/v1/leads/{id}/convert - Convert Lead to Member")
    void testConvertLeadToMember() throws Exception {
        LeadCreateRequest createReq = buildSampleLeadRequest();
        createReq.setEmail("rahul.convert@example.com");
        createReq.setQuotedFee(new BigDecimal("1500"));
        createReq.setAdvanceToken(new BigDecimal("500"));

        MvcResult createResult = mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andReturn();

        String leadId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .path("data").path("id").asText();

        LeadConvertToMemberRequest convertReq = new LeadConvertToMemberRequest();
        convertReq.setRecordPayment(true);
        convertReq.setAmountPaid(new BigDecimal("1500"));

        mockMvc.perform(post("/api/v1/leads/" + leadId + "/convert")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(convertReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.fullName", is("Rahul Sharma")))
                .andExpect(jsonPath("$.data.email", is("rahul.convert@example.com")));

        // Verify lead is marked CONVERTED
        mockMvc.perform(get("/api/v1/leads/" + leadId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status", is("CONVERTED")))
                .andExpect(jsonPath("$.data.convertedMemberId", notNullValue()));
    }

    @Test
    @DisplayName("9. GET /api/v1/leads/export/csv - Export CSV")
    void testExportCsv() throws Exception {
        LeadCreateRequest createReq = buildSampleLeadRequest();

        mockMvc.perform(post("/api/v1/leads")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/leads/export/csv"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", containsString("leads_export.csv")))
                .andExpect(content().contentType("text/csv;charset=UTF-8"))
                .andExpect(content().string(containsString("Lead Code,Full Name,Phone,Email")))
                .andExpect(content().string(containsString("Rahul Sharma")));
    }
}
