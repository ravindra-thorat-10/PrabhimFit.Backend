package com.example.prabhim.controller;

import java.time.LocalDate;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
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
import com.example.prabhim.dto.VerifyEmailRequest;
import com.example.prabhim.dto.profile.ChangePasswordRequest;
import com.example.prabhim.dto.profile.ProfileUpdateRequest;
import com.example.prabhim.entity.Member;
import com.example.prabhim.entity.Membership;
import com.example.prabhim.entity.User;
import com.example.prabhim.entity.enums.Gender;
import com.example.prabhim.entity.enums.MemberStatus;
import com.example.prabhim.entity.enums.MembershipStatus;
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
    private MemberRepository memberRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MemberActivityLogRepository memberActivityLogRepository;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    @DisplayName("1. GET /api/v1/auth/me - Retrieve Profile for Logged In User")
    void testGetMeSuccess() throws Exception {
        String accessToken = createAndLoginUser("admin@prabhimfit.com", "AdminPass123!", "Vishal", "Jagdhane");

        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.email", is("admin@prabhimfit.com")))
                .andExpect(jsonPath("$.data.name", is("Vishal Jagdhane")))
                .andExpect(jsonPath("$.data.user.email", is("admin@prabhimfit.com")))
                .andExpect(jsonPath("$.data.user.name", is("Vishal Jagdhane")))
                .andExpect(jsonPath("$.data.role", is("ADMIN")));
    }

    @Test
    @DisplayName("2. PUT /api/v1/auth/profile - Update Profile with Frontend Member/Admin Fields")
    void testUpdateAuthProfile() throws Exception {
        String accessToken = createAndLoginUser("coach@prabhimfit.com", "CoachPass123!", "Rahul", "Sharma");

        ProfileUpdateRequest updateReq = new ProfileUpdateRequest();
        updateReq.setName("Rahul V. Sharma");
        updateReq.setPhone("+91 9876543210");
        updateReq.setAvatar("https://images.unsplash.com/photo-1534528741775-53994a69daeb");
        updateReq.setAddress("Flat 402, Rohan Heights, Baner Road, Pune 411045");
        updateReq.setDesignation("Head Strength Coach");
        updateReq.setCity("Pune");
        updateReq.setState("Maharashtra");
        updateReq.setPincode("411045");
        updateReq.setBio("Certified strength coach with 10+ years experience.");

        mockMvc.perform(put("/api/v1/auth/profile")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("Rahul V. Sharma")))
                .andExpect(jsonPath("$.data.phone", is("+91 9876543210")))
                .andExpect(jsonPath("$.data.avatar", containsString("unsplash.com")))
                .andExpect(jsonPath("$.data.address", containsString("Baner Road")))
                .andExpect(jsonPath("$.data.designation", is("Head Strength Coach")))
                .andExpect(jsonPath("$.data.city", is("Pune")))
                .andExpect(jsonPath("$.data.user.phone", is("+91 9876543210")));
    }

    @Test
    @DisplayName("3. GET /api/v1/profile and /me - Dedicated Profile Endpoints")
    void testDedicatedProfileEndpoint() throws Exception {
        String accessToken = createAndLoginUser("owner@prabhimfit.com", "OwnerPass123!", "Suresh", "Patil");

        mockMvc.perform(get("/api/v1/profile")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("Suresh Patil")))
                .andExpect(jsonPath("$.data.adminProfile", notNullValue()));

        mockMvc.perform(get("/api/v1/profile/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.email", is("owner@prabhimfit.com")));
    }

    @Test
    @DisplayName("4. PUT /api/v1/profile - Update via Dedicated Profile Controller")
    void testDedicatedProfileUpdate() throws Exception {
        String accessToken = createAndLoginUser("admin2@prabhimfit.com", "AdminPass123!", "Anil", "Deshmukh");

        ProfileUpdateRequest updateReq = new ProfileUpdateRequest();
        updateReq.setName("Anil Kumar Deshmukh");
        updateReq.setPhone("+91 9123456789");
        updateReq.setAdminId("ADM-2024-999");
        updateReq.setGymLogo("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==");

        mockMvc.perform(put("/api/v1/profile")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("Anil Kumar Deshmukh")))
                .andExpect(jsonPath("$.data.admin_id", is("ADM-2024-999")))
                .andExpect(jsonPath("$.data.gym_logo", containsString("data:image/png;base64")));
    }

    @Test
    @DisplayName("5. PUT & POST /api/v1/auth/change-password - Change Password Flow")
    void testChangePasswordSuccess() throws Exception {
        String accessToken = createAndLoginUser("secure@prabhimfit.com", "OldPassword123!", "Secure", "User");

        ChangePasswordRequest changeReq = new ChangePasswordRequest(
                "OldPassword123!",
                "NewSecurePassword456!",
                "NewSecurePassword456!"
        );

        mockMvc.perform(put("/api/v1/auth/change-password")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("Password changed successfully")));

        // Verify login with new password succeeds
        LoginRequest newLoginReq = new LoginRequest("secure@prabhimfit.com", "NewSecurePassword456!");
        mockMvc.perform(post("/api/v1/auth/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newLoginReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    @DisplayName("6. Change Password - Incorrect current password returns error")
    void testChangePasswordIncorrectCurrentPassword() throws Exception {
        String accessToken = createAndLoginUser("secure2@prabhimfit.com", "OldPassword123!", "Secure", "Two");

        ChangePasswordRequest changeReq = new ChangePasswordRequest(
                "WrongCurrentPassword!",
                "NewPassword456!",
                "NewPassword456!"
        );

        mockMvc.perform(put("/api/v1/auth/change-password")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeReq)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    @DisplayName("7. Change Password - Mismatch between new and confirmation password")
    void testChangePasswordMismatch() throws Exception {
        String accessToken = createAndLoginUser("secure3@prabhimfit.com", "OldPassword123!", "Secure", "Three");

        ChangePasswordRequest changeReq = new ChangePasswordRequest(
                "OldPassword123!",
                "NewPassword456!",
                "MismatchedPassword789!"
        );

        mockMvc.perform(put("/api/v1/auth/change-password")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("confirmation password do not match")));
    }

    @Test
    @DisplayName("8. Profile with Linked Member Details (MemberProfilePage support)")
    void testProfileLinkedMemberDetails() throws Exception {
        String email = "athlete@prabhimfit.com";
        String accessToken = createAndLoginUser(email, "AthletePass123!", "Virat", "Kohli");

        // Create linked member entity
        Member member = new Member();
        member.setMemberCode("FC-1001");
        member.setFirstName("Virat");
        member.setLastName("Kohli");
        member.setEmail(email);
        member.setPhone("+91 9999999999");
        member.setAddress("42 Fitness Enclave, Pune");
        member.setJoinDate(LocalDate.now().minusMonths(3));
        member.setStatus(MemberStatus.ACTIVE);
        member.setGender(Gender.MALE);
        memberRepository.save(member);

        Membership ms = new Membership();
        ms.setMember(member);
        ms.setPlanName("Pro Elite Annual Plan");
        ms.setPlanType("PREMIUM");
        ms.setStartDate(LocalDate.now().minusMonths(3));
        ms.setEndDate(LocalDate.now().plusMonths(9));
        ms.setStatus(MembershipStatus.ACTIVE);
        membershipRepository.save(ms);

        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.memberProfile.memberId", is("FC-1001")))
                .andExpect(jsonPath("$.data.memberProfile.status", is("ACTIVE")))
                .andExpect(jsonPath("$.data.memberProfile.membershipPlan.name", is("Pro Elite Annual Plan")))
                .andExpect(jsonPath("$.data.user.memberProfile.memberId", is("FC-1001")));
    }

    @Test
    @DisplayName("9. POST /api/v1/profile/avatar and /logo")
    void testAvatarAndLogoUpload() throws Exception {
        String accessToken = createAndLoginUser("branding@prabhimfit.com", "BrandPass123!", "Logo", "Admin");

        // Avatar upload
        mockMvc.perform(post("/api/v1/profile/avatar")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("avatar", "https://prabhimfit.com/avatar.jpg"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.avatar", is("https://prabhimfit.com/avatar.jpg")));

        // Logo upload
        mockMvc.perform(post("/api/v1/profile/logo")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("gymLogo", "https://prabhimfit.com/logo.png"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.gym_logo", is("https://prabhimfit.com/logo.png")));
    }

    @Test
    @DisplayName("10. Unauthenticated Access is Rejected (401)")
    void testUnauthenticatedAccessRejected() throws Exception {
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success", is(false)));

        mockMvc.perform(put("/api/v1/auth/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success", is(false)));

        mockMvc.perform(get("/api/v1/profile"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success", is(false)));
    }

    private String createAndLoginUser(String email, String password, String firstName, String lastName) throws Exception {
        RegisterRequest registerReq = new RegisterRequest();
        registerReq.setEmail(email);
        registerReq.setPassword(password);
        registerReq.setPasswordConfirm(password);
        registerReq.setFirstName(firstName);
        registerReq.setLastName(lastName);
        registerReq.setPhone("+91 9876543210");

        mockMvc.perform(post("/api/v1/auth/register/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerReq)));

        User user = userRepository.findByEmail(email).orElseThrow();
        mockMvc.perform(post("/api/v1/auth/verify-email/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new VerifyEmailRequest(email, user.getVerificationOtp()))));

        LoginRequest loginReq = new LoginRequest(email, password);
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        return jsonNode.path("data").path("access").asText();
    }
}
