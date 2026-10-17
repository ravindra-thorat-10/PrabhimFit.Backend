package com.example.prabhim.service.impl;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prabhim.dto.UserResponse;
import com.example.prabhim.dto.profile.ChangePasswordRequest;
import com.example.prabhim.dto.profile.ProfileResponse;
import com.example.prabhim.dto.profile.ProfileUpdateRequest;
import com.example.prabhim.entity.Member;
import com.example.prabhim.entity.Membership;
import com.example.prabhim.entity.Trainer;
import com.example.prabhim.entity.User;
import com.example.prabhim.exception.InvalidCredentialsException;
import com.example.prabhim.exception.UserNotFoundException;
import com.example.prabhim.repository.MemberRepository;
import com.example.prabhim.repository.MembershipRepository;
import com.example.prabhim.repository.TrainerRepository;
import com.example.prabhim.repository.UserRepository;
import com.example.prabhim.service.AuthService;
import com.example.prabhim.service.ProfileService;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final MembershipRepository membershipRepository;
    private final TrainerRepository trainerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public ProfileServiceImpl(
            UserRepository userRepository,
            MemberRepository memberRepository,
            MembershipRepository membershipRepository,
            TrainerRepository trainerRepository,
            PasswordEncoder passwordEncoder,
            AuthService authService) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.membershipRepository = membershipRepository;
        this.trainerRepository = trainerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse getProfile(UUID userId, HttpServletRequest httpRequest) {
        User user = resolveUser(userId, httpRequest);
        return mapToProfileResponse(user);
    }

    @Override
    @Transactional
    public ProfileResponse updateProfile(UUID userId, ProfileUpdateRequest request, HttpServletRequest httpRequest) {
        User user = resolveUser(userId, httpRequest);

        if (request != null) {
            // Name handling
            if (request.getName() != null && !request.getName().trim().isEmpty()) {
                user.setFullName(request.getName().trim());
            }
            if (request.getFirstName() != null && !request.getFirstName().trim().isEmpty()) {
                user.setFirstName(request.getFirstName().trim());
            }
            if (request.getLastName() != null) {
                user.setLastName(request.getLastName().trim());
            }

            // Phone
            if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
                user.setPhone(request.getPhone().trim());
            }
            if (request.getAlternatePhone() != null) {
                user.setAlternatePhone(request.getAlternatePhone().trim());
            }

            // Avatar / Profile Photo
            if (request.getAvatar() != null && !request.getAvatar().trim().isEmpty()) {
                user.setAvatar(request.getAvatar().trim());
                user.setProfileImage(request.getAvatar().trim());
                user.setProfilePic(request.getAvatar().trim());
            }

            // Designation & Role
            if (request.getDesignation() != null) {
                user.setDesignation(request.getDesignation().trim());
            }
            if (request.getRole() != null && !request.getRole().trim().isEmpty()) {
                user.setRole(request.getRole().trim().toUpperCase());
            }

            // Address & Location
            if (request.getAddress() != null) {
                user.setAddress(request.getAddress().trim());
            }
            if (request.getCity() != null) {
                user.setCity(request.getCity().trim());
            }
            if (request.getState() != null) {
                user.setState(request.getState().trim());
            }
            if (request.getPincode() != null) {
                user.setPincode(request.getPincode().trim());
            }

            // Personal details
            if (request.getGender() != null) {
                user.setGender(request.getGender().trim());
            }
            if (request.getDob() != null) {
                user.setDob(request.getDob().trim());
            }

            // Emergency contact
            if (request.getEmergencyContactName() != null) {
                user.setEmergencyContactName(request.getEmergencyContactName().trim());
            }
            if (request.getEmergencyContactPhone() != null) {
                user.setEmergencyContactPhone(request.getEmergencyContactPhone().trim());
            }
            if (request.getEmergencyContactRelation() != null) {
                user.setEmergencyContactRelation(request.getEmergencyContactRelation().trim());
            }

            // Administrative & bio
            if (request.getBio() != null) {
                user.setBio(request.getBio().trim());
            }
            if (request.getAdminId() != null) {
                user.setAdminId(request.getAdminId().trim());
            }
            if (request.getAdminSince() != null) {
                user.setAdminSince(request.getAdminSince().trim());
            }

            // Notification & security preferences
            if (request.getWhatsappAlerts() != null) {
                user.setWhatsappAlerts(request.getWhatsappAlerts());
            }
            if (request.getEmailNotifications() != null) {
                user.setEmailNotifications(request.getEmailNotifications());
            }
            if (request.getTwoFactorEnabled() != null) {
                user.setTwoFactorEnabled(request.getTwoFactorEnabled());
            }

            // Gym brand logo
            if (request.getGymLogo() != null) {
                user.setGymLogo(request.getGymLogo().trim());
            }

            user.setUpdatedAt(LocalDateTime.now());
            user = userRepository.save(user);

            // Synchronize with linked Member entity if present
            syncWithMember(user, request);

            // Synchronize with linked Trainer entity if present
            syncWithTrainer(user, request);
        }

        return mapToProfileResponse(user);
    }

    @Override
    @Transactional
    public void changePassword(UUID userId, ChangePasswordRequest request, HttpServletRequest httpRequest) {
        User user = resolveUser(userId, httpRequest);

        if (request == null || request.getCurrentPassword() == null || request.getNewPassword() == null) {
            throw new IllegalArgumentException("Current password and new password are required.");
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Current password does not match our records.");
        }

        if (request.getConfirmPassword() != null && !request.getConfirmPassword().isBlank()) {
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                throw new IllegalArgumentException("New password and confirmation password do not match.");
            }
        }

        if (request.getNewPassword().length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters long.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public ProfileResponse updateAvatar(UUID userId, String avatar, HttpServletRequest httpRequest) {
        User user = resolveUser(userId, httpRequest);
        user.setAvatar(avatar);
        user.setProfileImage(avatar);
        user.setProfilePic(avatar);
        user.setUpdatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        // Update member/trainer if linked
        if (user.getEmail() != null) {
            memberRepository.findByEmail(user.getEmail()).ifPresent(m -> {
                m.setProfileImage(avatar);
                memberRepository.save(m);
            });
            trainerRepository.findByEmail(user.getEmail()).ifPresent(t -> {
                t.setAvatar(avatar);
                trainerRepository.save(t);
            });
        }

        return mapToProfileResponse(user);
    }

    @Override
    @Transactional
    public ProfileResponse updateGymLogo(UUID userId, String gymLogo, HttpServletRequest httpRequest) {
        User user = resolveUser(userId, httpRequest);
        user.setGymLogo(gymLogo);
        user.setUpdatedAt(LocalDateTime.now());
        user = userRepository.save(user);
        return mapToProfileResponse(user);
    }

    /**
     * Resolves the current user from authenticated principal, SecurityContext,
     * or tenant identity request headers.
     */
    public User resolveUser(UUID userId, HttpServletRequest request) {
        // 1. Try explicit userId parameter
        if (userId != null) {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                return userOpt.get();
            }
        }

        // 2. Try SecurityContextHolder authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UUID uId) {
                Optional<User> userOpt = userRepository.findById(uId);
                if (userOpt.isPresent()) {
                    return userOpt.get();
                }
            } else if (principal instanceof String strPrincipal) {
                try {
                    UUID uId = UUID.fromString(strPrincipal);
                    Optional<User> userOpt = userRepository.findById(uId);
                    if (userOpt.isPresent()) {
                        return userOpt.get();
                    }
                } catch (IllegalArgumentException ignored) {
                    if (strPrincipal.contains("@")) {
                        Optional<User> userOpt = userRepository.findByEmail(strPrincipal.trim());
                        if (userOpt.isPresent()) {
                            return userOpt.get();
                        }
                    }
                }
            }
        }

        // 3. Try tenant headers from request
        if (request != null) {
            String tenantEmail = getHeaderEmail(request);
            if (tenantEmail != null && !tenantEmail.isBlank()) {
                Optional<User> userOpt = userRepository.findByEmail(tenantEmail.trim().toLowerCase());
                if (userOpt.isPresent()) {
                    return userOpt.get();
                }
            }
        }

        // 4. Default admin fallback if database is brand new (demo safety)
        if (userRepository.count() == 0) {
            User defaultAdmin = new User();
            defaultAdmin.setEmail("admin@prabhimfit.com");
            defaultAdmin.setFirstName("Vishal");
            defaultAdmin.setLastName("Jagdhane");
            defaultAdmin.setPhone("+91 94033 01412");
            defaultAdmin.setDesignation("Managing Director & Chief Administrator");
            defaultAdmin.setRole("ADMIN");
            defaultAdmin.setAdminId("ADM-2024-001");
            defaultAdmin.setPassword(passwordEncoder.encode("Admin@123"));
            defaultAdmin.setEmailVerified(true);
            defaultAdmin.setActive(true);
            defaultAdmin.setStaff(true);
            defaultAdmin.setSuperuser(true);
            defaultAdmin.setCreatedAt(LocalDateTime.now());
            defaultAdmin.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(defaultAdmin);
        }

        throw new UserNotFoundException("Authenticated user could not be located. Please provide valid credentials.");
    }

    private String getHeaderEmail(HttpServletRequest request) {
        String[] headers = {"X-Tenant-ID", "X-Creator-Email", "X-Gym-ID", "X-User-Email"};
        for (String h : headers) {
            String val = request.getHeader(h);
            if (val != null && val.contains("@")) {
                return val.trim();
            }
        }
        return null;
    }

    /**
     * Maps User entity and any linked Member / Trainer data to ProfileResponse.
     */
    public ProfileResponse mapToProfileResponse(User user) {
        UserResponse userResponse = authService.mapToUserResponse(user);

        ProfileResponse response = new ProfileResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setName(user.getFullName());
        response.setFullName(user.getFullName());
        response.setPhone(user.getPhone());
        response.setAlternatePhone(user.getAlternatePhone());
        response.setDesignation(user.getDesignation());
        response.setRole(user.getRole() != null ? user.getRole() : "ADMIN");
        response.setAdminId(user.getAdminId());
        response.setAdminSince(user.getAdminSince());
        response.setAvatar(user.getAvatar());
        response.setProfileImage(user.getProfileImage());
        response.setAddress(user.getAddress());
        response.setCity(user.getCity());
        response.setState(user.getState());
        response.setPincode(user.getPincode());
        response.setGender(user.getGender());
        response.setDob(user.getDob());
        response.setEmergencyContactName(user.getEmergencyContactName());
        response.setEmergencyContactPhone(user.getEmergencyContactPhone());
        response.setEmergencyContactRelation(user.getEmergencyContactRelation());
        response.setBio(user.getBio());
        response.setWhatsappAlerts(user.getWhatsappAlerts());
        response.setEmailNotifications(user.getEmailNotifications());
        response.setTwoFactorEnabled(user.getTwoFactorEnabled());
        response.setGymLogo(user.getGymLogo());
        response.setEmailVerified(user.isEmailVerified());
        response.setActive(user.isActive());
        response.setStaff(user.isStaff());
        response.setSuperuser(user.isSuperuser());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        // Construct Admin Profile object
        Map<String, Object> adminProfile = new LinkedHashMap<>();
        adminProfile.put("name", user.getFullName());
        adminProfile.put("email", user.getEmail());
        adminProfile.put("phone", user.getPhone() != null ? user.getPhone() : "");
        adminProfile.put("alternatePhone", user.getAlternatePhone() != null ? user.getAlternatePhone() : "");
        adminProfile.put("designation", user.getDesignation() != null ? user.getDesignation() : "Chief Gym Operations Director");
        adminProfile.put("role", user.getRole() != null ? user.getRole() : "ADMIN");
        adminProfile.put("adminId", user.getAdminId() != null ? user.getAdminId() : "ADM-001");
        adminProfile.put("avatar", user.getAvatar() != null ? user.getAvatar() : "");
        adminProfile.put("address", user.getAddress() != null ? user.getAddress() : "");
        adminProfile.put("city", user.getCity() != null ? user.getCity() : "");
        adminProfile.put("state", user.getState() != null ? user.getState() : "");
        adminProfile.put("pincode", user.getPincode() != null ? user.getPincode() : "");
        adminProfile.put("gender", user.getGender() != null ? user.getGender() : "");
        adminProfile.put("dob", user.getDob() != null ? user.getDob() : "");
        adminProfile.put("emergencyContactName", user.getEmergencyContactName() != null ? user.getEmergencyContactName() : "");
        adminProfile.put("emergencyContactPhone", user.getEmergencyContactPhone() != null ? user.getEmergencyContactPhone() : "");
        adminProfile.put("emergencyContactRelation", user.getEmergencyContactRelation() != null ? user.getEmergencyContactRelation() : "");
        adminProfile.put("bio", user.getBio() != null ? user.getBio() : "");
        adminProfile.put("adminSince", user.getAdminSince() != null ? user.getAdminSince() : "");
        adminProfile.put("whatsappAlerts", user.getWhatsappAlerts() != null ? user.getWhatsappAlerts() : true);
        adminProfile.put("emailNotifications", user.getEmailNotifications() != null ? user.getEmailNotifications() : true);
        adminProfile.put("twoFactorEnabled", user.getTwoFactorEnabled() != null ? user.getTwoFactorEnabled() : false);
        adminProfile.put("gymLogo", user.getGymLogo() != null ? user.getGymLogo() : "");

        response.setAdminProfile(adminProfile);
        userResponse.setAdminProfile(adminProfile);

        // Check if a linked Member record exists (for MemberProfilePage)
        if (user.getEmail() != null) {
            Optional<Member> memberOpt = memberRepository.findByEmail(user.getEmail());
            if (memberOpt.isPresent()) {
                Member member = memberOpt.get();
                Map<String, Object> memberProfile = new LinkedHashMap<>();
                memberProfile.put("id", member.getId());
                memberProfile.put("_id", member.getId().toString());
                memberProfile.put("memberId", member.getMemberCode());
                memberProfile.put("status", member.getStatus() != null ? member.getStatus().name() : "ACTIVE");
                memberProfile.put("startDate", member.getJoinDate());
                memberProfile.put("address", member.getAddress() != null ? member.getAddress() : user.getAddress());

                // Find active or latest membership plan
                Optional<Membership> msOpt = membershipRepository.findLatestMembershipByMemberId(member.getId());
                if (msOpt.isPresent()) {
                    Membership ms = msOpt.get();
                    memberProfile.put("endDate", ms.getEndDate());
                    Map<String, Object> planMap = new LinkedHashMap<>();
                    planMap.put("name", ms.getPlanName());
                    planMap.put("type", ms.getPlanType() != null ? ms.getPlanType() : "STANDARD");
                    memberProfile.put("membershipPlan", planMap);
                } else {
                    Map<String, Object> defaultPlan = new LinkedHashMap<>();
                    defaultPlan.put("name", "Standard Membership");
                    defaultPlan.put("type", "STANDARD");
                    memberProfile.put("membershipPlan", defaultPlan);
                }

                // Trainer info
                if (member.getTrainer() != null) {
                    Map<String, Object> trainerMap = new LinkedHashMap<>();
                    trainerMap.put("id", member.getTrainer().getId());
                    trainerMap.put("name", member.getTrainer().getFullName());
                    trainerMap.put("email", member.getTrainer().getEmail());
                    trainerMap.put("phone", member.getTrainer().getPhone());
                    trainerMap.put("avatar", member.getTrainer().getAvatar());
                    memberProfile.put("assignedTrainer", trainerMap);
                }

                response.setMemberProfile(memberProfile);
                userResponse.setMemberProfile(memberProfile);
            }
        }

        response.setUser(userResponse);
        return response;
    }

    private void syncWithMember(User user, ProfileUpdateRequest request) {
        if (user.getEmail() == null) return;
        memberRepository.findByEmail(user.getEmail()).ifPresent(member -> {
            if (user.getFirstName() != null) member.setFirstName(user.getFirstName());
            if (user.getLastName() != null) member.setLastName(user.getLastName());
            if (user.getPhone() != null) member.setPhone(user.getPhone());
            if (user.getAddress() != null) member.setAddress(user.getAddress());
            if (user.getAvatar() != null) member.setProfileImage(user.getAvatar());
            if (user.getEmergencyContactName() != null) member.setEmergencyContactName(user.getEmergencyContactName());
            if (user.getEmergencyContactPhone() != null) member.setEmergencyContactPhone(user.getEmergencyContactPhone());
            memberRepository.save(member);
        });
    }

    private void syncWithTrainer(User user, ProfileUpdateRequest request) {
        if (user.getEmail() == null) return;
        trainerRepository.findByEmail(user.getEmail()).ifPresent(trainer -> {
            if (user.getFullName() != null) trainer.setFullName(user.getFullName());
            if (user.getPhone() != null) trainer.setPhone(user.getPhone());
            if (user.getAvatar() != null) trainer.setAvatar(user.getAvatar());
            if (user.getBio() != null) trainer.setBio(user.getBio());
            if (user.getDesignation() != null) trainer.setSpecialization(user.getDesignation());
            trainerRepository.save(trainer);
        });
    }
}
