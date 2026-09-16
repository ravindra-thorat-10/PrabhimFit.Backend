package com.example.prabhim.controller;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.prabhim.dto.LoginRequest;
import com.example.prabhim.dto.RegisterRequest;
import com.example.prabhim.dto.attendance.AttendanceCheckOutRequest;
import com.example.prabhim.dto.attendance.AttendanceMarkRequest;
import com.example.prabhim.entity.User;
import com.example.prabhim.repository.AttendanceRepository;
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
class AttendanceControllerTest {

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

    @Autowired
    private TrainerRepository trainerRepository;

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
        trainerRepository.deleteAll();
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
    @DisplayName("1. Mark Attendance (POST /api/v1/attendance)")
    void testMarkAttendance() throws Exception {
        AttendanceMarkRequest request = new AttendanceMarkRequest();
        request.setMemberCode("FC-1009");
        request.setSessionDateStr("15-09-2026");
        request.setCheckInTime("07:14 pm");
        request.setStatus("PRESENT");
        request.setFacility("Downtown Flagship - Studio Turnstiles");
        request.setVerifiedBy("Marcus Vance");

        mockMvc.perform(post("/api/v1/attendance")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.memberCode", is("FC-1009")))
                .andExpect(jsonPath("$.data.memberName", is("Zoya Akhtar")))
                .andExpect(jsonPath("$.data.checkInTimeFormatted", containsString("07:14")))
                .andExpect(jsonPath("$.data.status", is("PRESENT")))
                .andExpect(jsonPath("$.data.verifiedBy", is("Marcus Vance")));
    }

    @Test
    @DisplayName("2. List All Attendance (GET /api/v1/attendance)")
    void testListAllAttendance() throws Exception {
        mockMvc.perform(get("/api/v1/attendance")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.content", notNullValue()))
                .andExpect(jsonPath("$.data.totalElements", is(6)))
                .andExpect(jsonPath("$.data.content[0].verifiedBy", is("Marcus Vance")))
                .andExpect(jsonPath("$.data.content[0].status", is("PRESENT")));
    }

    @Test
    @DisplayName("3. Get One Attendance Using ID (GET /api/v1/attendance/{id})")
    void testGetAttendanceById() throws Exception {
        MvcResult listResult = mockMvc.perform(get("/api/v1/attendance")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode json = objectMapper.readTree(listResult.getResponse().getContentAsString());
        String attendanceIdStr = json.get("data").get("content").get(0).get("id").asText();
        UUID attendanceId = UUID.fromString(attendanceIdStr);

        mockMvc.perform(get("/api/v1/attendance/" + attendanceId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(attendanceIdStr)))
                .andExpect(jsonPath("$.data.status", is("PRESENT")));
    }

    @Test
    @DisplayName("4. Edit Attendance & Delete Attendance (PUT/PATCH/DELETE /api/v1/attendance/{id})")
    void testEditAndDeleteAttendance() throws Exception {
        MvcResult listResult = mockMvc.perform(get("/api/v1/attendance")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode json = objectMapper.readTree(listResult.getResponse().getContentAsString());
        String attendanceIdStr = json.get("data").get("content").get(0).get("id").asText();
        UUID attendanceId = UUID.fromString(attendanceIdStr);

        // Edit via PUT
        AttendanceMarkRequest updateReq = new AttendanceMarkRequest();
        updateReq.setStatus("LATE");
        updateReq.setFacility("Main Studio");
        updateReq.setVerifiedBy("Director");

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/v1/attendance/" + attendanceId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.status", is("LATE")))
                .andExpect(jsonPath("$.data.facility", is("Main Studio")))
                .andExpect(jsonPath("$.data.verifiedBy", is("Director")));

        // Edit via PATCH
        AttendanceMarkRequest patchReq = new AttendanceMarkRequest();
        patchReq.setStatus("EXCUSED");

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/v1/attendance/" + attendanceId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.status", is("EXCUSED")));

        // Delete
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/v1/attendance/" + attendanceId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        // Verify 404 after deletion
        mockMvc.perform(get("/api/v1/attendance/" + attendanceId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }
}
