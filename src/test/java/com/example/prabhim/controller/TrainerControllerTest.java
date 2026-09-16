package com.example.prabhim.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.example.prabhim.dto.trainer.TrainerCreateRequest;
import com.example.prabhim.dto.trainer.TrainerUpdateRequest;
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
class TrainerControllerTest {

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
    @DisplayName("1. Get All Trainers & Verify Auto Seeded Records")
    void testGetAllTrainersAutoSeeded() throws Exception {
        mockMvc.perform(get("/api/v1/trainers")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.content", hasSize(3)))
                .andExpect(jsonPath("$.data.content[0].trainerCode", is("TR-101")))
                .andExpect(jsonPath("$.data.content[0].fullName", is("Vikram Malhotra")))
                .andExpect(jsonPath("$.data.content[0].specialization", is("Strength & Conditioning")))
                .andExpect(jsonPath("$.data.content[0].monthlySalary", is(38000.00)))
                .andExpect(jsonPath("$.data.content[1].trainerCode", is("TR-102")))
                .andExpect(jsonPath("$.data.content[1].fullName", is("Priya Sharma")))
                .andExpect(jsonPath("$.data.content[1].monthlySalary", is(45000.00)))
                .andExpect(jsonPath("$.data.content[2].trainerCode", is("TR-103")))
                .andExpect(jsonPath("$.data.content[2].fullName", is("Rohan Joshi")))
                .andExpect(jsonPath("$.data.content[2].specialization", is("Postural Rehab & Mobility")))
                .andExpect(jsonPath("$.data.content[2].monthlySalary", is(40000.00)));
    }

    @Test
    @DisplayName("2. Get One Trainer Using ID")
    void testGetTrainerById() throws Exception {
        mockMvc.perform(get("/api/v1/trainers")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk());

        var vikram = trainerRepository.findByTrainerCode("TR-101").orElseThrow();

        mockMvc.perform(get("/api/v1/trainers/" + vikram.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(vikram.getId().toString())))
                .andExpect(jsonPath("$.data.trainerCode", is("TR-101")))
                .andExpect(jsonPath("$.data.fullName", is("Vikram Malhotra")));
    }

    @Test
    @DisplayName("3. Add New Trainer")
    void testAddTrainer() throws Exception {
        TrainerCreateRequest createReq = new TrainerCreateRequest();
        createReq.setTrainerFullName("Kabir Bedi");
        createReq.setEmail("kabir@gym.com");
        createReq.setPhone("+91 98224 11223");
        createReq.setSpecialization("Calisthenics & Bodyweight");
        createReq.setExperience("4 Years");
        createReq.setMonthlySalary(new BigDecimal("42000.00"));
        createReq.setShift("Morning (06:00 - 14:00)");
        createReq.setJoiningDate(LocalDate.of(2026, 9, 15));
        createReq.setProfessionalBio("Master of calisthenics, core stability, and gymnastics training.");
        createReq.setStatus("ACTIVE");

        MvcResult result = mockMvc.perform(post("/api/v1/trainers")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", notNullValue()))
                .andExpect(jsonPath("$.data.trainerCode", is("TR-104")))
                .andExpect(jsonPath("$.data.fullName", is("Kabir Bedi")))
                .andExpect(jsonPath("$.data.monthlySalary", is(42000.00)))
                .andExpect(jsonPath("$.data.monthlySalaryFormatted", is("₹42,000")))
                .andExpect(jsonPath("$.data.specialization", is("Calisthenics & Bodyweight")))
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        String trainerIdStr = json.get("data").get("id").asText();
        UUID trainerId = UUID.fromString(trainerIdStr);

        // Get by ID
        mockMvc.perform(get("/api/v1/trainers/" + trainerId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fullName", is("Kabir Bedi")));
    }

    @Test
    @DisplayName("4. Edit Trainer Profile & Delete")
    void testEditAndDeleteTrainer() throws Exception {
        // Find Rohan Joshi (TR-103)
        mockMvc.perform(get("/api/v1/trainers"))
                .andExpect(status().isOk());

        var rohan = trainerRepository.findByTrainerCode("TR-103").orElseThrow();

        // Edit Trainer
        TrainerUpdateRequest editReq = new TrainerUpdateRequest();
        editReq.setFullName("Rohan Joshi");
        editReq.setPhoneNumber("+91 98223 44556");
        editReq.setSpecialization("Postural Rehab & Mobility");
        editReq.setExperience("5 Years");
        editReq.setMonthlySalary(new BigDecimal("40000.00"));
        editReq.setStatus("ACTIVE");
        editReq.setBio("Kinesiologist focusing on joint health, corrective exercise patterns, and posture optimization.");

        mockMvc.perform(put("/api/v1/trainers/" + rohan.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fullName", is("Rohan Joshi")))
                .andExpect(jsonPath("$.data.specialization", is("Postural Rehab & Mobility")))
                .andExpect(jsonPath("$.data.monthlySalary", is(40000.00)))
                .andExpect(jsonPath("$.data.status", is("ACTIVE")));

        // Patch status
        TrainerUpdateRequest patchReq = new TrainerUpdateRequest();
        patchReq.setStatus("ON_LEAVE");

        mockMvc.perform(patch("/api/v1/trainers/" + rohan.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status", is("ON_LEAVE")));

        // Delete Trainer
        mockMvc.perform(delete("/api/v1/trainers/" + rohan.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        // Verify 404 after delete
        mockMvc.perform(get("/api/v1/trainers/" + rohan.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }
}
