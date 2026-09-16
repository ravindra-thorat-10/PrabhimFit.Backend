package com.example.prabhim.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prabhim.dto.LoginRequest;
import com.example.prabhim.dto.LoginResponse;
import com.example.prabhim.dto.LogoutRequest;
import com.example.prabhim.dto.RefreshTokenRequest;
import com.example.prabhim.dto.RegisterRequest;
import com.example.prabhim.dto.ResendOtpRequest;
import com.example.prabhim.dto.ResetPasswordRequest;
import com.example.prabhim.dto.SessionResponse;
import com.example.prabhim.dto.TokenRefreshResponse;
import com.example.prabhim.dto.UserResponse;
import com.example.prabhim.dto.VerifyEmailRequest;
import com.example.prabhim.entity.Session;
import com.example.prabhim.entity.User;
import com.example.prabhim.exception.InvalidCredentialsException;
import com.example.prabhim.exception.InvalidOtpException;
import com.example.prabhim.exception.OtpExpiredException;
import com.example.prabhim.exception.UserAlreadyExistsException;
import com.example.prabhim.exception.UserNotFoundException;
import com.example.prabhim.repository.SessionRepository;
import com.example.prabhim.repository.UserRepository;
import com.example.prabhim.security.JwtService;
import com.example.prabhim.util.UserAgentUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SessionRepository sessionRepository;
    private final EmailService emailService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            SessionRepository sessionRepository,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.sessionRepository = sessionRepository;
        this.emailService = emailService;
    }

    public UserResponse register(RegisterRequest request) {
        if (request.getPassword() != null && !request.getPassword().equals(request.getPasswordConfirm())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmailVerified(false);
        user.setActive(true);
        user.setStaff(false);
        user.setSuperuser(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        String otp = String.format("%06d", (int) (Math.random() * 900000) + 100000);
        user.setVerificationOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

        emailService.sendOtpEmail(user.getEmail(), otp, "registration");

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    public UserResponse verifyEmail(VerifyEmailRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.isEmailVerified()) {
            throw new UserAlreadyExistsException("Email is already verified");
        }
        if (user.getVerificationOtp() == null) {
            throw new InvalidOtpException("OTP not found");
        }
        if (!user.getVerificationOtp().equals(request.getOtp())) {
            throw new InvalidOtpException("Invalid OTP");
        }
        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new OtpExpiredException("OTP has expired");
        }

        user.setEmailVerified(true);
        user.setVerificationOtp(null);
        user.setOtpExpiry(null);
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    public void resendOtp(ResendOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String purpose = request.getPurpose();
        if (purpose == null || (!purpose.equals("registration") && !purpose.equals("password_reset"))) {
            throw new InvalidOtpException("Invalid purpose. Use registration or password_reset");
        }
        if (purpose.equals("registration") && user.isEmailVerified()) {
            throw new UserAlreadyExistsException("Email is already verified");
        }

        String otp = String.format("%06d", (int) (Math.random() * 900000) + 100000);
        user.setVerificationOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        user.setUpdatedAt(LocalDateTime.now());

        emailService.sendOtpEmail(user.getEmail(), otp, purpose);
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        if (!user.isEmailVerified()) {
            throw new InvalidCredentialsException("Email not verified. Please verify your email first.");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Record session
        Session session = new Session();
        session.setUserId(user.getId());
        session.setRefreshToken(refreshToken);
        session.setIpAddress(UserAgentUtils.getClientIp(httpRequest));
        session.setDeviceType(UserAgentUtils.getDeviceType(httpRequest));
        session.setBrowser(UserAgentUtils.getBrowser(httpRequest));
        session.setOs(UserAgentUtils.getOs(httpRequest));
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusDays(7));
        session.setCurrent(true);
        sessionRepository.save(session);

        LoginResponse response = new LoginResponse();
        response.setAccess(accessToken);
        response.setRefresh(refreshToken);
        response.setUser(mapToUserResponse(user));

        return response;
    }

    @Transactional
    public TokenRefreshResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefresh();
        if (refreshToken == null || !jwtService.validateRefreshToken(refreshToken)) {
            throw new InvalidCredentialsException("Invalid or expired refresh token");
        }

        String newAccess = jwtService.generateAccessTokenFromRefresh(refreshToken);
        String newRefresh = jwtService.rotateRefreshToken(refreshToken);

        // Update stored session if present
        Optional<Session> sessionOpt = sessionRepository.findByRefreshToken(refreshToken);
        if (sessionOpt.isPresent()) {
            Session session = sessionOpt.get();
            session.setRefreshToken(newRefresh);
            session.setExpiresAt(LocalDateTime.now().plusDays(7));
            sessionRepository.save(session);
        }

        return new TokenRefreshResponse(newAccess, newRefresh);
    }

    @Transactional
    public void logout(LogoutRequest request, UUID currentUserId) {
        if (request != null && request.getRefresh() != null && !request.getRefresh().isBlank()) {
            sessionRepository.deleteByRefreshToken(request.getRefresh());
        } else if (currentUserId != null) {
            List<Session> sessions = sessionRepository.findByUserId(currentUserId);
            if (!sessions.isEmpty()) {
                sessionRepository.delete(sessions.get(0));
            }
        }
    }

    public List<SessionResponse> getSessions(UUID userId) {
        return sessionRepository.findByUserId(userId)
                .stream()
                .map(this::mapToSessionResponse)
                .toList();
    }

    @Transactional
    public void logoutAll(UUID userId) {
        sessionRepository.deleteByUserId(userId);
    }

    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getVerificationOtp() == null) {
            throw new InvalidOtpException("OTP not found");
        }
        if (!user.getVerificationOtp().equals(request.getOtp())) {
            throw new InvalidOtpException("Invalid OTP");
        }
        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new OtpExpiredException("OTP has expired");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setVerificationOtp(null);
        user.setOtpExpiry(null);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhone(user.getPhone());
        response.setProfileImage(user.getProfileImage());
        response.setEmailVerified(user.isEmailVerified());
        response.setActive(user.isActive());
        response.setStaff(user.isStaff());
        response.setSuperuser(user.isSuperuser());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    public SessionResponse mapToSessionResponse(Session session) {
        SessionResponse response = new SessionResponse();
        response.setId(session.getId());
        response.setIpAddress(session.getIpAddress());
        response.setDeviceType(session.getDeviceType());
        response.setBrowser(session.getBrowser());
        response.setOs(session.getOs());
        response.setCreatedAt(session.getCreatedAt());
        response.setExpiresAt(session.getExpiresAt());
        response.setCurrent(session.isCurrent());
        return response;
    }
}
