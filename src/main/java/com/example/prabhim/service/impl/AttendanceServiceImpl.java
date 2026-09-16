package com.example.prabhim.service.impl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prabhim.dto.attendance.AttendanceCheckOutRequest;
import com.example.prabhim.dto.attendance.AttendanceMarkRequest;
import com.example.prabhim.dto.attendance.AttendanceResponse;
import com.example.prabhim.dto.attendance.AttendanceStatsResponse;
import com.example.prabhim.entity.Attendance;
import com.example.prabhim.entity.Member;
import com.example.prabhim.entity.enums.Gender;
import com.example.prabhim.entity.enums.MemberStatus;
import com.example.prabhim.exception.AttendanceNotFoundException;
import com.example.prabhim.exception.MemberNotFoundException;
import com.example.prabhim.repository.AttendanceRepository;
import com.example.prabhim.repository.MemberRepository;
import com.example.prabhim.service.AttendanceService;

import jakarta.annotation.PostConstruct;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;

    public AttendanceServiceImpl(
            AttendanceRepository attendanceRepository,
            MemberRepository memberRepository) {
        this.attendanceRepository = attendanceRepository;
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    @Override
    public void seedDefaultAttendanceIfEmpty() {
        if (memberRepository.count() == 0) {
            seedDefaultMembers();
        }

        if (attendanceRepository.count() == 0) {
            LocalDate today = LocalDate.now();

            Member tanvi = memberRepository.findByMemberCode("FC-1008").orElse(null);
            Member rhea = memberRepository.findByMemberCode("FC-1006").orElse(null);
            Member ananya = memberRepository.findByMemberCode("FC-1004").orElse(null);
            Member kabir = memberRepository.findByMemberCode("FC-1003").orElse(null);
            Member neha = memberRepository.findByMemberCode("FC-1002").orElse(null);
            Member arjun = memberRepository.findByMemberCode("FC-1001").orElse(null);

            List<Attendance> records = new ArrayList<>();

            if (tanvi != null) {
                // Tanvi: checked in at 06:51 PM today, not yet checked out
                Attendance a1 = new Attendance(
                        tanvi,
                        LocalDateTime.of(today, LocalTime.of(18, 51)),
                        null,
                        "PRESENT",
                        "Marcus Vance",
                        "Downtown Flagship - Studio Turnstiles"
                );
                records.add(a1);
            }

            if (rhea != null) {
                Attendance a2 = new Attendance(
                        rhea,
                        LocalDateTime.of(today, LocalTime.of(6, 30)),
                        LocalDateTime.of(today, LocalTime.of(7, 45)),
                        "PRESENT",
                        "Marcus Vance",
                        "Downtown Flagship - Studio Turnstiles"
                );
                records.add(a2);
            }

            if (ananya != null) {
                Attendance a3 = new Attendance(
                        ananya,
                        LocalDateTime.of(today, LocalTime.of(6, 30)),
                        LocalDateTime.of(today, LocalTime.of(7, 45)),
                        "PRESENT",
                        "Marcus Vance",
                        "Downtown Flagship - Studio Turnstiles"
                );
                records.add(a3);
            }

            if (kabir != null) {
                Attendance a4 = new Attendance(
                        kabir,
                        LocalDateTime.of(today, LocalTime.of(6, 30)),
                        LocalDateTime.of(today, LocalTime.of(7, 45)),
                        "PRESENT",
                        "Marcus Vance",
                        "Downtown Flagship - Studio Turnstiles"
                );
                records.add(a4);
            }

            if (neha != null) {
                Attendance a5 = new Attendance(
                        neha,
                        LocalDateTime.of(today, LocalTime.of(6, 30)),
                        LocalDateTime.of(today, LocalTime.of(7, 45)),
                        "PRESENT",
                        "Marcus Vance",
                        "Downtown Flagship - Studio Turnstiles"
                );
                records.add(a5);
            }

            if (arjun != null) {
                Attendance a6 = new Attendance(
                        arjun,
                        LocalDateTime.of(today, LocalTime.of(6, 0)),
                        LocalDateTime.of(today, LocalTime.of(7, 15)),
                        "PRESENT",
                        "Marcus Vance",
                        "Downtown Flagship - Studio Turnstiles"
                );
                records.add(a6);
            }

            if (!records.isEmpty()) {
                attendanceRepository.saveAllAndFlush(records);
            }
        }
    }

    private void seedDefaultMembers() {
        List<Member> list = new ArrayList<>();
        list.add(createMemberSeed("Arjun", "Kapoor", "arjun@example.com", "+91 98221 00101", Gender.MALE, "FC-1001"));
        list.add(createMemberSeed("Neha", "Kulkarni", "neha@example.com", "+91 98221 00102", Gender.FEMALE, "FC-1002"));
        list.add(createMemberSeed("Kabir", "Singhania", "kabir.s@example.com", "+91 98221 00103", Gender.MALE, "FC-1003"));
        list.add(createMemberSeed("Ananya", "Deshmukh", "ananya@example.com", "+91 98221 00104", Gender.FEMALE, "FC-1004"));
        list.add(createMemberSeed("Meera", "Patel", "meera@example.com", "+91 98221 00105", Gender.FEMALE, "FC-1005"));
        list.add(createMemberSeed("Rhea", "Sen", "rhea@example.com", "+91 98221 00106", Gender.FEMALE, "FC-1006"));
        list.add(createMemberSeed("Dev", "Nair", "dev@example.com", "+91 98221 00107", Gender.MALE, "FC-1007"));
        list.add(createMemberSeed("Tanvi", "Agarwal", "tanvi@example.com", "+91 98221 00108", Gender.FEMALE, "FC-1008"));
        list.add(createMemberSeed("Zoya", "Akhtar", "zoya@example.com", "+91 98221 00109", Gender.FEMALE, "FC-1009"));
        memberRepository.saveAllAndFlush(list);
    }

    private Member createMemberSeed(String first, String last, String email, String phone, Gender gender, String code) {
        Member m = new Member();
        m.setFirstName(first);
        m.setLastName(last);
        m.setEmail(email);
        m.setPhone(phone);
        m.setGender(gender);
        m.setMemberCode(code);
        m.setStatus(MemberStatus.ACTIVE);
        m.setJoinDate(LocalDate.of(2026, 1, 10));
        return m;
    }

    @Override
    public Page<AttendanceResponse> getAttendance(String search, LocalDate date, String status, Pageable pageable) {
        seedDefaultAttendanceIfEmpty();

        LocalDate targetDate = date != null ? date : LocalDate.now();
        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.atTime(LocalTime.MAX);

        Pageable effectivePageable = pageable != null ? pageable : PageRequest.of(0, 20);
        Page<Attendance> page = attendanceRepository.findByFilters(startOfDay, endOfDay, search, status, effectivePageable);
        return page.map(AttendanceResponse::fromEntity);
    }

    @Override
    public AttendanceStatsResponse getAttendanceStats(LocalDate date) {
        seedDefaultAttendanceIfEmpty();

        LocalDate targetDate = date != null ? date : LocalDate.now();
        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.atTime(LocalTime.MAX);

        AttendanceStatsResponse stats = new AttendanceStatsResponse();
        stats.setOnTimeCheckInsToday(attendanceRepository.countOnTimeCheckInsBetween(startOfDay, endOfDay));
        stats.setLateArrivalsToday(attendanceRepository.countLateArrivalsBetween(startOfDay, endOfDay));
        stats.setTotalRegisteredAthletes(memberRepository.count());
        stats.setSelectedDate(targetDate);
        return stats;
    }

    @Override
    public AttendanceResponse markAttendance(AttendanceMarkRequest request) {
        seedDefaultAttendanceIfEmpty();

        Member member = null;
        UUID memberId = request.resolveMemberId();
        if (memberId != null) {
            member = memberRepository.findById(memberId).orElse(null);
        }
        if (member == null && request.getMemberCode() != null) {
            member = memberRepository.findByMemberCode(request.getMemberCode().trim()).orElse(null);
        }
        if (member == null && request.getMemberName() != null) {
            String name = request.getMemberName().trim();
            member = memberRepository.findAll().stream()
                    .filter(m -> (m.getFirstName() + " " + (m.getLastName() != null ? m.getLastName() : "")).trim().equalsIgnoreCase(name))
                    .findFirst()
                    .orElse(null);
        }

        if (member == null) {
            throw new MemberNotFoundException("Member not found for attendance record");
        }

        LocalDate date = request.resolveSessionDate();
        LocalDateTime checkIn = request.resolveCheckInTime(date);
        LocalDateTime checkOut = request.resolveCheckOutTime(date);

        Attendance attendance = new Attendance();
        attendance.setMember(member);
        attendance.setCheckInTime(checkIn);
        attendance.setCheckOutTime(checkOut);
        attendance.setStatus(request.getStatus() != null ? request.getStatus().toUpperCase() : "PRESENT");
        attendance.setFacility(request.getFacility() != null ? request.getFacility() : "Downtown Flagship - Studio Turnstiles");
        attendance.setVerifiedBy(request.getVerifiedBy() != null ? request.getVerifiedBy() : "Marcus Vance");
        attendance.setMethod("MANUAL");

        Attendance saved = attendanceRepository.save(attendance);
        return AttendanceResponse.fromEntity(saved);
    }

    @Override
    public AttendanceResponse getAttendanceById(UUID id) {
        seedDefaultAttendanceIfEmpty();
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new AttendanceNotFoundException("Attendance record not found with id: " + id));
        return AttendanceResponse.fromEntity(attendance);
    }

    @Override
    public AttendanceResponse updateAttendance(UUID id, AttendanceMarkRequest request) {
        seedDefaultAttendanceIfEmpty();
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new AttendanceNotFoundException("Attendance record not found with id: " + id));

        if (request != null) {
            LocalDate date = request.getSessionDate() != null ? request.resolveSessionDate() : attendance.getCheckInTime().toLocalDate();

            if (request.getCheckInTime() != null && !request.getCheckInTime().trim().isEmpty()) {
                attendance.setCheckInTime(request.resolveCheckInTime(date));
            }
            if (request.getCheckOutTime() != null && !request.getCheckOutTime().trim().isEmpty()) {
                attendance.setCheckOutTime(request.resolveCheckOutTime(date));
            }
            if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
                attendance.setStatus(request.getStatus().trim().toUpperCase());
            }
            if (request.getFacility() != null && !request.getFacility().trim().isEmpty()) {
                attendance.setFacility(request.getFacility().trim());
            }
            if (request.getVerifiedBy() != null && !request.getVerifiedBy().trim().isEmpty()) {
                attendance.setVerifiedBy(request.getVerifiedBy().trim());
            }
        }

        Attendance saved = attendanceRepository.save(attendance);
        return AttendanceResponse.fromEntity(saved);
    }

    @Override
    public void deleteAttendance(UUID id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new AttendanceNotFoundException("Attendance record not found with id: " + id));
        attendanceRepository.delete(attendance);
    }

    @Override
    public AttendanceResponse checkOut(UUID id, AttendanceCheckOutRequest request) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new AttendanceNotFoundException("Attendance record not found with id: " + id));

        LocalDateTime checkOutTime = LocalDateTime.now();
        if (request != null && request.getCheckOutDateTime() != null) {
            checkOutTime = request.getCheckOutDateTime();
        } else if (request != null && request.getCheckOutTime() != null && !request.getCheckOutTime().trim().isEmpty()) {
            try {
                String str = request.getCheckOutTime().trim();
                LocalTime lt;
                if (str.toUpperCase().contains("AM") || str.toUpperCase().contains("PM")) {
                    lt = LocalTime.parse(str.toUpperCase(), DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH));
                } else {
                    lt = LocalTime.parse(str, DateTimeFormatter.ofPattern("HH:mm"));
                }
                checkOutTime = LocalDateTime.of(attendance.getCheckInTime().toLocalDate(), lt);
            } catch (Exception ignored) {
            }
        }

        attendance.setCheckOutTime(checkOutTime);
        Attendance saved = attendanceRepository.save(attendance);
        return AttendanceResponse.fromEntity(saved);
    }

    @Override
    public byte[] exportAttendanceCsv(LocalDate date) {
        seedDefaultAttendanceIfEmpty();

        LocalDate targetDate = date != null ? date : LocalDate.now();
        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.atTime(LocalTime.MAX);

        List<Attendance> records = attendanceRepository.findAllForExport(startOfDay, endOfDay);

        StringBuilder csv = new StringBuilder();
        csv.append("Attendance ID,Member Code,Member Name,Phone,Check-In Time,Check-Out Time,Status,Verified By,Facility\n");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a", Locale.ENGLISH);

        for (Attendance a : records) {
            String memCode = a.getMember() != null ? a.getMember().getMemberCode() : "";
            String memName = a.getMember() != null
                    ? (a.getMember().getFirstName() + " " + (a.getMember().getLastName() != null ? a.getMember().getLastName() : "")).trim()
                    : "";
            String phone = a.getMember() != null ? a.getMember().getPhone() : "";
            String inTime = a.getCheckInTime() != null ? a.getCheckInTime().format(dtf) : "";
            String outTime = a.getCheckOutTime() != null ? a.getCheckOutTime().format(dtf) : "ACTIVE SESSION";
            String status = a.getStatus();
            String verified = a.getVerifiedBy() != null ? a.getVerifiedBy() : "";
            String facility = a.getFacility() != null ? a.getFacility() : "";

            csv.append(escapeCsv(a.getId().toString())).append(",")
                    .append(escapeCsv(memCode)).append(",")
                    .append(escapeCsv(memName)).append(",")
                    .append(escapeCsv(phone)).append(",")
                    .append(escapeCsv(inTime)).append(",")
                    .append(escapeCsv(outTime)).append(",")
                    .append(escapeCsv(status)).append(",")
                    .append(escapeCsv(verified)).append(",")
                    .append(escapeCsv(facility)).append("\n");
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escapeCsv(String val) {
        if (val == null) return "";
        if (val.contains(",") || val.contains("\"") || val.contains("\n")) {
            return "\"" + val.replace("\"", "\"\"") + "\"";
        }
        return val;
    }
}
