package com.example.prabhim.controller;

import java.util.Map;

import static org.hamcrest.Matchers.containsString;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.prabhim.dto.LoginRequest;
import com.example.prabhim.dto.LogoutRequest;
import com.example.prabhim.dto.RefreshTokenRequest;
import com.example.prabhim.dto.RegisterRequest;
import com.example.prabhim.dto.ResendOtpRequest;
import com.example.prabhim.dto.VerifyEmailRequest;
import com.example.prabhim.entity.User;
import com.example.prabhim.repository.SessionRepository;
import com.example.prabhim.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class AuthControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("1. Register API - Success")
    void testRegisterSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("user@example.com");
        request.setPassword("Password123!");
        request.setPasswordConfirm("Password123!");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhone("+1234567890");

        mockMvc.perform(post("/api/v1/auth/register/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("User registered successfully")))
                .andExpect(jsonPath("$.data.email", is("user@example.com")))
                .andExpect(jsonPath("$.data.first_name", is("John")))
                .andExpect(jsonPath("$.data.last_name", is("Doe")))
                .andExpect(jsonPath("$.data.phone", is("+1234567890")))
                .andExpect(jsonPath("$.data.is_email_verified", is(false)))
                .andExpect(jsonPath("$.data.is_active", is(true)))
                .andExpect(jsonPath("$.data.is_staff", is(false)))
                .andExpect(jsonPath("$.data.is_superuser", is(false)))
                .andExpect(jsonPath("$.data.id", notNullValue()));
    }

    @Test
    @DisplayName("1b. Register API - Password mismatch validation")
    void testRegisterPasswordMismatch() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("user@example.com");
        request.setPassword("Password123!");
        request.setPasswordConfirm("DifferentPassword!");
        request.setFirstName("John");
        request.setLastName("Doe");

        mockMvc.perform(post("/api/v1/auth/register/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Passwords do not match")));
    }

    @Test
    @DisplayName("2. Verify Email API - Success")
    void testVerifyEmailSuccess() throws Exception {
        // Register first
        RegisterRequest registerReq = new RegisterRequest();
        registerReq.setEmail("user@example.com");
        registerReq.setPassword("Password123!");
        registerReq.setPasswordConfirm("Password123!");
        registerReq.setFirstName("John");
        registerReq.setLastName("Doe");
        registerReq.setPhone("+1234567890");

        mockMvc.perform(post("/api/v1/auth/register/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerReq)));

        User user = userRepository.findByEmail("user@example.com").orElseThrow();
        String otp = user.getVerificationOtp();

        VerifyEmailRequest verifyReq = new VerifyEmailRequest("user@example.com", otp);

        mockMvc.perform(post("/api/v1/auth/verify-email/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("Email verified successfully")))
                .andExpect(jsonPath("$.data.is_email_verified", is(true)))
                .andExpect(jsonPath("$.data.email", is("user@example.com")));
    }

    @Test
    @DisplayName("3. Resend OTP API - Success")
    void testResendOtpSuccess() throws Exception {
        RegisterRequest registerReq = new RegisterRequest();
        registerReq.setEmail("user@example.com");
        registerReq.setPassword("Password123!");
        registerReq.setPasswordConfirm("Password123!");
        registerReq.setFirstName("John");
        registerReq.setLastName("Doe");

        mockMvc.perform(post("/api/v1/auth/register/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerReq)));

        ResendOtpRequest resendReq = new ResendOtpRequest("user@example.com", "registration");

        mockMvc.perform(post("/api/v1/auth/resend-otp/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resendReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Verification OTP sent to your email.")));
    }

    @Test
    @DisplayName("4. Login API - Success")
    void testLoginSuccess() throws Exception {
        // Register and verify
        RegisterRequest registerReq = new RegisterRequest();
        registerReq.setEmail("user@example.com");
        registerReq.setPassword("Password123!");
        registerReq.setPasswordConfirm("Password123!");
        registerReq.setFirstName("John");
        registerReq.setLastName("Doe");
        registerReq.setPhone("+1234567890");

        mockMvc.perform(post("/api/v1/auth/register/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerReq)));

        User user = userRepository.findByEmail("user@example.com").orElseThrow();
        mockMvc.perform(post("/api/v1/auth/verify-email/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new VerifyEmailRequest("user@example.com", user.getVerificationOtp()))));

        // Login
        LoginRequest loginReq = new LoginRequest("user@example.com", "Password123!");

        mockMvc.perform(post("/api/v1/auth/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Logged in successfully.")))
                .andExpect(jsonPath("$.data.access", notNullValue()))
                .andExpect(jsonPath("$.data.refresh", notNullValue()))
                .andExpect(jsonPath("$.data.user.email", is("user@example.com")))
                .andExpect(jsonPath("$.data.user.is_email_verified", is(true)));
    }

    @Test
    @DisplayName("5. Refresh Token API - Success")
    void testRefreshTokenSuccess() throws Exception {
        // Setup authenticated user
        String refreshToken = setupAndLoginUser();

        RefreshTokenRequest refreshReq = new RefreshTokenRequest(refreshToken);

        mockMvc.perform(post("/api/v1/auth/token/refresh/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Token refreshed successfully.")))
                .andExpect(jsonPath("$.data.access", notNullValue()))
                .andExpect(jsonPath("$.data.refresh", notNullValue()));
    }

    @Test
    @DisplayName("6. Logout API - Success with Bearer Token")
    void testLogoutSuccess() throws Exception {
        Map<String, String> tokens = setupAndLoginUserWithTokens();
        String accessToken = tokens.get("access");
        String refreshToken = tokens.get("refresh");

        LogoutRequest logoutReq = new LogoutRequest(refreshToken);

        mockMvc.perform(post("/api/v1/auth/logout/")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Logged out successfully.")));
    }

    @Test
    @DisplayName("7. Get Sessions API - Success with Bearer Token")
    void testGetSessionsSuccess() throws Exception {
        Map<String, String> tokens = setupAndLoginUserWithTokens();
        String accessToken = tokens.get("access");

        mockMvc.perform(get("/api/v1/auth/sessions/")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Active sessions retrieved successfully.")))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].ip_address", notNullValue()))
                .andExpect(jsonPath("$.data[0].device_type", notNullValue()))
                .andExpect(jsonPath("$.data[0].is_current", is(true)));
    }

    @Test
    @DisplayName("8. Logout All Sessions API - Success with Bearer Token")
    void testLogoutAllSuccess() throws Exception {
        Map<String, String> tokens = setupAndLoginUserWithTokens();
        String accessToken = tokens.get("access");

        mockMvc.perform(post("/api/v1/auth/logout-all/")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Logged out from all active sessions successfully.")));
    }

    @Test
    @DisplayName("Protected Endpoints - Unauthorized without token")
    void testProtectedEndpointsWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/auth/sessions/"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success", is(false)));

        mockMvc.perform(post("/api/v1/auth/logout/"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success", is(false)));

        mockMvc.perform(post("/api/v1/auth/logout-all/"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success", is(false)));
    }

    private String setupAndLoginUser() throws Exception {
        return setupAndLoginUserWithTokens().get("refresh");
    }

    private Map<String, String> setupAndLoginUserWithTokens() throws Exception {
        RegisterRequest registerReq = new RegisterRequest();
        registerReq.setEmail("user@example.com");
        registerReq.setPassword("Password123!");
        registerReq.setPasswordConfirm("Password123!");
        registerReq.setFirstName("John");
        registerReq.setLastName("Doe");
        registerReq.setPhone("+1234567890");

        mockMvc.perform(post("/api/v1/auth/register/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerReq)));

        User user = userRepository.findByEmail("user@example.com").orElseThrow();
        mockMvc.perform(post("/api/v1/auth/verify-email/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new VerifyEmailRequest("user@example.com", user.getVerificationOtp()))));

        LoginRequest loginReq = new LoginRequest("user@example.com", "Password123!");
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        String access = jsonNode.path("data").path("access").asText();
        String refresh = jsonNode.path("data").path("refresh").asText();

        return Map.of("access", access, "refresh", refresh);
    }
}
