package com.example.prabhim.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
import com.example.prabhim.dto.diet.DietProtocolCreateRequest;
import com.example.prabhim.dto.diet.DietProtocolUpdateRequest;
import com.example.prabhim.entity.User;
import com.example.prabhim.repository.AttendanceRepository;
import com.example.prabhim.repository.DietPlanRepository;
import com.example.prabhim.repository.MemberActivityLogRepository;
import com.example.prabhim.repository.MemberRepository;
import com.example.prabhim.repository.MembershipPlanRepository;
import com.example.prabhim.repository.MembershipRepository;
import com.example.prabhim.repository.PaymentRepository;
import com.example.prabhim.repository.SessionRepository;
import com.example.prabhim.repository.TrainerRepository;
import com.example.prabhim.repository.UserRepository;
import com.example.prabhim.repository.WorkoutExerciseRepository;
import com.example.prabhim.repository.WorkoutRoutineRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class DietControllerTest {

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

    @Autowired
    private WorkoutRoutineRepository workoutRoutineRepository;

    @Autowired
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Autowired
    private DietPlanRepository dietPlanRepository;

    private String authToken;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        dietPlanRepository.deleteAll();
        workoutExerciseRepository.deleteAll();
        workoutRoutineRepository.deleteAll();
        attendanceRepository.deleteAll();
        membershipRepository.deleteAll();
        paymentRepository.deleteAll();
        memberActivityLogRepository.deleteAll();
        memberRepository.deleteAll();
        membershipPlanRepository.deleteAll();
        trainerRepository.deleteAll();
        sessionRepository.deleteAll();
        userRepository.deleteAll();

        RegisterRequest registerReq = new RegisterRequest();
        registerReq.setEmail("nutritionist@gymos.com");
        registerReq.setPassword("Admin@12345");
        registerReq.setPasswordConfirm("Admin@12345");
        registerReq.setFirstName("Pooja");
        registerReq.setLastName("Makhija");

        mockMvc.perform(post("/api/v1/auth/register/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isCreated());

        User user = userRepository.findByEmail("nutritionist@gymos.com").orElseThrow();
        user.setEmailVerified(true);
        user.setStaff(true);
        userRepository.save(user);

        LoginRequest loginReq = new LoginRequest("nutritionist@gymos.com", "Admin@12345");
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        this.authToken = root.get("data").get("access").asText();
    }

    @Test
    @DisplayName("Should successfully create a diet protocol with all nutrition fields, supplements, and weekly meal plan")
    void testCreateDietPlan() throws Exception {
        DietProtocolCreateRequest req = new DietProtocolCreateRequest();
        req.setName("High-Protein Clean Muscle Protocol");
        req.setAthleteName("Aarav Patel");
        req.setCoachName("Pooja Makhija");
        req.setGoal("Lean Muscle Gain & Recomposition");
        req.setPreference("Non-Vegetarian");
        req.setCalorieTarget(2500);
        req.setDailyWaterTarget(3.5);
        req.setProteinGrams(180);
        req.setCarbsGrams(260);
        req.setFatGrams(65);
        req.setBreakfastProtocol("3 Scrambled Eggs + 2 Slices Whole Grain Toast + Avocado + Black Coffee");
        req.setLunchProtocol("200g Grilled Chicken + 150g Brown Rice + Mixed Greens + Dal");
        req.setDinnerProtocol("Steamed Fish + 2 Rotis + Sautéed Zucchini");
        req.setSnacksProtocol("1 Apple with peanut butter + Whey Shake");
        req.setSpecialGuidance("Maintain steady hydration throughout strength sessions.");
        req.setSupplements(Arrays.asList("Whey Protein Isolate", "Creatine Monohydrate"));

        Map<String, Object> weeklyPlan = new HashMap<>();
        Map<String, Object> mondayPlan = new HashMap<>();
        mondayPlan.put("focus", "High-Protein Fuel");
        mondayPlan.put("calories", 2500);
        weeklyPlan.put("monday", mondayPlan);
        req.setWeeklyMealPlan(weeklyPlan);

        mockMvc.perform(post("/api/v1/diets")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("High-Protein Clean Muscle Protocol")))
                .andExpect(jsonPath("$.data.athleteName", is("Aarav Patel")))
                .andExpect(jsonPath("$.data.goal", is("Lean Muscle Gain & Recomposition")))
                .andExpect(jsonPath("$.data.preference", is("Non-Vegetarian")))
                .andExpect(jsonPath("$.data.dailyCalories", is(2500)))
                .andExpect(jsonPath("$.data.calorieDisplay", is("2500 KCAL")))
                .andExpect(jsonPath("$.data.dailyWaterTarget", is(3.5)))
                .andExpect(jsonPath("$.data.waterDisplay", is("3.5L WATER")))
                .andExpect(jsonPath("$.data.proteinGrams", is(180)))
                .andExpect(jsonPath("$.data.carbsGrams", is(260)))
                .andExpect(jsonPath("$.data.fatGrams", is(65)))
                .andExpect(jsonPath("$.data.supplements", notNullValue()))
                .andExpect(jsonPath("$.data.weeklyMealPlan", notNullValue()))
                .andExpect(jsonPath("$.data.creatorEmail", notNullValue()));
    }

    @Test
    @DisplayName("Should list all diet protocols with search and retrieve stats")
    void testListDietPlansAndStats() throws Exception {
        DietProtocolCreateRequest req1 = new DietProtocolCreateRequest();
        req1.setName("High-Protein Clean Muscle Protocol");
        req1.setAthleteName("Aarav Patel");
        req1.setCalorieTarget(2500);
        req1.setDailyWaterTarget(3.5);
        mockMvc.perform(post("/api/v1/diets")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req1))).andExpect(status().isCreated());

        DietProtocolCreateRequest req2 = new DietProtocolCreateRequest();
        req2.setName("Keto Fat-Loss & Shred");
        req2.setAthleteName("Vishal Jagdhane");
        req2.setCalorieTarget(1950);
        req2.setDailyWaterTarget(4.0);
        mockMvc.perform(post("/api/v1/diets")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req2))).andExpect(status().isCreated());

        // GET all (permitAll)
        mockMvc.perform(get("/api/v1/diets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.totalElements", is(2)));

        // GET with search
        mockMvc.perform(get("/api/v1/diets?search=Keto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements", is(1)))
                .andExpect(jsonPath("$.data.content[0].name", is("Keto Fat-Loss & Shred")));

        // GET stats
        mockMvc.perform(get("/api/v1/diets/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.totalProtocols", is(2)))
                .andExpect(jsonPath("$.data.activeProtocols", is(2)));
    }

    @Test
    @DisplayName("Should update and delete diet protocol")
    void testUpdateAndDeleteDietPlan() throws Exception {
        DietProtocolCreateRequest req = new DietProtocolCreateRequest();
        req.setName("Initial Diet Protocol");
        req.setCalorieTarget(2000);

        MvcResult createResult = mockMvc.perform(post("/api/v1/diets")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn();

        String dietId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .path("data").path("id").asText();

        // Update
        DietProtocolUpdateRequest updateReq = new DietProtocolUpdateRequest();
        updateReq.setName("Updated Protocol Title");
        updateReq.setCalorieTarget(2200);
        updateReq.setGoal("Hypertrophy");

        mockMvc.perform(put("/api/v1/diets/" + dietId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is("Updated Protocol Title")))
                .andExpect(jsonPath("$.data.dailyCalories", is(2200)))
                .andExpect(jsonPath("$.data.goal", is("Hypertrophy")));

        // Update status
        mockMvc.perform(patch("/api/v1/diets/" + dietId + "/status?status=INACTIVE")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status", is("INACTIVE")));

        // Delete
        mockMvc.perform(delete("/api/v1/diets/" + dietId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        // Verify 404
        mockMvc.perform(get("/api/v1/diets/" + dietId))
                .andExpect(status().isNotFound());
    }
}
