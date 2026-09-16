package com.example.prabhim.controller;

import java.util.Arrays;
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
import com.example.prabhim.dto.workout.WorkoutExerciseRequest;
import com.example.prabhim.dto.workout.WorkoutRoutineCreateRequest;
import com.example.prabhim.dto.workout.WorkoutRoutineUpdateRequest;
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
import com.example.prabhim.repository.WorkoutExerciseRepository;
import com.example.prabhim.repository.WorkoutRoutineRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class WorkoutControllerTest {

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

    private String authToken;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

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
        registerReq.setEmail("coach@gymos.com");
        registerReq.setPassword("Admin@12345");
        registerReq.setPasswordConfirm("Admin@12345");
        registerReq.setFirstName("Vikram");
        registerReq.setLastName("Malhotra");

        mockMvc.perform(post("/api/v1/auth/register/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isCreated());

        User user = userRepository.findByEmail("coach@gymos.com").orElseThrow();
        user.setEmailVerified(true);
        user.setStaff(true);
        userRepository.save(user);

        LoginRequest loginReq = new LoginRequest("coach@gymos.com", "Admin@12345");
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        this.authToken = root.get("data").get("access").asText();
    }

    @Test
    @DisplayName("Should successfully create a workout routine with exercises roster")
    void testCreateWorkoutRoutine() throws Exception {
        WorkoutRoutineCreateRequest req = new WorkoutRoutineCreateRequest();
        req.setName("Back & Biceps Power Builder");
        req.setAthleteName("Aarav Patel");
        req.setCoachName("Vikram Malhotra");
        req.setTrainingGoal("V-Taper Lat Width & Biceps Density");
        req.setDurationWeeks(6);
        req.setStatus("ACTIVE");
        req.setInstructions("Stay hydrated and prioritize eccentric control.");

        WorkoutExerciseRequest ex1 = new WorkoutExerciseRequest("Barbell Deadlifts", 4, "6-8", "120 kg", 1);
        WorkoutExerciseRequest ex2 = new WorkoutExerciseRequest("Weighted Pull-ups", 3, "8-10", "+5 kg", 2);
        WorkoutExerciseRequest ex3 = new WorkoutExerciseRequest("Seated Cable Rows", 3, "10-12", "55 kg", 3);
        WorkoutExerciseRequest ex4 = new WorkoutExerciseRequest("Incline Dumbbell Curls", 3, "10-12", "14 kg each", 4);

        req.setExercises(Arrays.asList(ex1, ex2, ex3, ex4));

        mockMvc.perform(post("/api/v1/workouts")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("Back & Biceps Power Builder")))
                .andExpect(jsonPath("$.data.athleteName", is("Aarav Patel")))
                .andExpect(jsonPath("$.data.coachName", is("Vikram Malhotra")))
                .andExpect(jsonPath("$.data.trainingGoal", is("V-Taper Lat Width & Biceps Density")))
                .andExpect(jsonPath("$.data.durationWeeks", is(6)))
                .andExpect(jsonPath("$.data.durationDisplay", is("6 Weeks Duration")))
                .andExpect(jsonPath("$.data.exerciseCount", is(4)))
                .andExpect(jsonPath("$.data.exercises", hasSize(4)))
                .andExpect(jsonPath("$.data.exercises[0].exerciseName", is("Barbell Deadlifts")))
                .andExpect(jsonPath("$.data.exercises[0].setsRepsDisplay", is("4 Sets × 6-8 Reps")))
                .andExpect(jsonPath("$.data.exercises[0].targetWeight", is("120 kg")));
    }

    @Test
    @DisplayName("Should list all workout routines with pagination and search filter")
    void testListWorkoutRoutines() throws Exception {
        // Create Routine 1
        WorkoutRoutineCreateRequest req1 = new WorkoutRoutineCreateRequest();
        req1.setName("Back & Biceps Power Builder");
        req1.setAthleteName("Aarav Patel");
        req1.setTrainingGoal("V-Taper Lat Width");
        req1.setDurationWeeks(6);
        mockMvc.perform(post("/api/v1/workouts")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req1))).andExpect(status().isCreated());

        // Create Routine 2
        WorkoutRoutineCreateRequest req2 = new WorkoutRoutineCreateRequest();
        req2.setName("Chest & Triceps Hypertrophy");
        req2.setAthleteName("Vishal Jagdhane");
        req2.setTrainingGoal("Chest Thickness & Triceps Volume");
        req2.setDurationWeeks(4);
        mockMvc.perform(post("/api/v1/workouts")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req2))).andExpect(status().isCreated());

        // Test GET all (permitAll)
        mockMvc.perform(get("/api/v1/workouts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.totalElements", is(2)));

        // Test GET with search
        mockMvc.perform(get("/api/v1/workouts?search=Chest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements", is(1)))
                .andExpect(jsonPath("$.data.content[0].name", is("Chest & Triceps Hypertrophy")));
    }

    @Test
    @DisplayName("Should get single workout routine by ID")
    void testGetWorkoutRoutineById() throws Exception {
        WorkoutRoutineCreateRequest req = new WorkoutRoutineCreateRequest();
        req.setName("Chest & Triceps Hypertrophy");
        req.setAthleteName("Vishal Jagdhane");
        req.setTrainingGoal("Chest Thickness & Triceps Volume");
        req.setDurationWeeks(4);

        WorkoutExerciseRequest ex1 = new WorkoutExerciseRequest("Barbell Flat Bench Press", 4, "8-10", "70 kg", 1);
        req.setExercises(Arrays.asList(ex1));

        MvcResult createResult = mockMvc.perform(post("/api/v1/workouts")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(createResult.getResponse().getContentAsString());
        String routineId = root.path("data").path("id").asText();

        mockMvc.perform(get("/api/v1/workouts/" + routineId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(routineId)))
                .andExpect(jsonPath("$.data.name", is("Chest & Triceps Hypertrophy")))
                .andExpect(jsonPath("$.data.exercises", hasSize(1)));
    }

    @Test
    @DisplayName("Should update workout routine and instructions")
    void testUpdateWorkoutRoutine() throws Exception {
        WorkoutRoutineCreateRequest req = new WorkoutRoutineCreateRequest();
        req.setName("Chest & Triceps Hypertrophy");
        req.setTrainingGoal("Chest Thickness");
        req.setInstructions("Initial instructions");

        MvcResult createResult = mockMvc.perform(post("/api/v1/workouts")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn();

        String routineId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .path("data").path("id").asText();

        WorkoutRoutineUpdateRequest updateReq = new WorkoutRoutineUpdateRequest();
        updateReq.setName("Chest & Triceps Hypertrophy (Updated)");
        updateReq.setTrainingGoal("Chest Thickness & Triceps Volume");
        updateReq.setInstructions("Warm up shoulders with band pull-aparts for 5 minutes before heavy pressing.");

        mockMvc.perform(put("/api/v1/workouts/" + routineId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("Chest & Triceps Hypertrophy (Updated)")))
                .andExpect(jsonPath("$.data.trainingGoal", is("Chest Thickness & Triceps Volume")))
                .andExpect(jsonPath("$.data.instructions", is("Warm up shoulders with band pull-aparts for 5 minutes before heavy pressing.")));
    }

    @Test
    @DisplayName("Should delete workout routine successfully and return 404 afterwards")
    void testDeleteWorkoutRoutine() throws Exception {
        WorkoutRoutineCreateRequest req = new WorkoutRoutineCreateRequest();
        req.setName("Routine to delete");

        MvcResult createResult = mockMvc.perform(post("/api/v1/workouts")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn();

        String routineId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .path("data").path("id").asText();

        mockMvc.perform(delete("/api/v1/workouts/" + routineId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        mockMvc.perform(get("/api/v1/workouts/" + routineId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should add and remove individual exercises in a routine")
    void testAddAndRemoveExercise() throws Exception {
        WorkoutRoutineCreateRequest req = new WorkoutRoutineCreateRequest();
        req.setName("Routine for movement test");

        MvcResult createResult = mockMvc.perform(post("/api/v1/workouts")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn();

        String routineId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .path("data").path("id").asText();

        // Add exercise
        WorkoutExerciseRequest ex = new WorkoutExerciseRequest("Incline Dumbbell Press", 3, "10-12", "22 kg", 1);
        MvcResult addResult = mockMvc.perform(post("/api/v1/workouts/" + routineId + "/exercises")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ex)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.exercises", hasSize(1)))
                .andReturn();

        String exerciseId = objectMapper.readTree(addResult.getResponse().getContentAsString())
                .path("data").path("exercises").get(0).path("id").asText();

        // Remove exercise
        mockMvc.perform(delete("/api/v1/workouts/" + routineId + "/exercises/" + exerciseId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.exercises", hasSize(0)));
    }
}
