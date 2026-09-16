package com.example.prabhim.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.prabhim.dto.ApiResponse;
import com.example.prabhim.dto.LoginRequest;
import com.example.prabhim.dto.LoginResponse;
import com.example.prabhim.dto.LogoutRequest;
import com.example.prabhim.dto.RefreshTokenRequest;
import com.example.prabhim.dto.RegisterRequest;
import com.example.prabhim.dto.ResendOtpRequest;
import com.example.prabhim.dto.SessionResponse;
import com.example.prabhim.dto.TokenRefreshResponse;
import com.example.prabhim.dto.UserResponse;
import com.example.prabhim.dto.VerifyEmailRequest;
import com.example.prabhim.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    
    @PostMapping("/register/")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("User registered successfully. A verification OTP has been sent to your email.", response)
        );
    }

    
    @PostMapping("/verify-email/")
    public ResponseEntity<ApiResponse<UserResponse>> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        UserResponse response = authService.verifyEmail(request);
        return ResponseEntity.ok(
                ApiResponse.success("Email verified successfully. You can now log in.", response)
        );
    }

    @PostMapping("/resend-otp/")
    public ResponseEntity<ApiResponse<Void>> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        authService.resendOtp(request);
        return ResponseEntity.ok(
                ApiResponse.success("Verification OTP sent to your email.")
        );
    }

    @PostMapping("/login/")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        LoginResponse response = authService.login(request, httpRequest);
        return ResponseEntity.ok(
                ApiResponse.success("Logged in successfully.", response)
        );
    }

    @PostMapping("/token/refresh/")
    public ResponseEntity<ApiResponse<TokenRefreshResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        TokenRefreshResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(
                ApiResponse.success("Token refreshed successfully.", response)
        );
    }

    @PostMapping("/logout/")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestBody(required = false) LogoutRequest request,
            @AuthenticationPrincipal UUID userId) {
        authService.logout(request, userId);
        return ResponseEntity.ok(
                ApiResponse.success("Logged out successfully.")
        );
    }

   
    @GetMapping("/sessions/")
    public ResponseEntity<ApiResponse<List<SessionResponse>>> getSessions(
            @AuthenticationPrincipal UUID userId) {
        List<SessionResponse> sessions = authService.getSessions(userId);
        return ResponseEntity.ok(
                ApiResponse.success("Active sessions retrieved successfully.", sessions)
        );
    }

    
    @PostMapping("/logout-all/")
    public ResponseEntity<ApiResponse<Void>> logoutAll(
            @AuthenticationPrincipal UUID userId) {
        authService.logoutAll(userId);
        return ResponseEntity.ok(
                ApiResponse.success("Logged out from all active sessions successfully.")
        );
    }
}
