package com.example.prabhim.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.prabhim.dto.ApiResponse;
import com.example.prabhim.dto.attendance.AttendanceCheckOutRequest;
import com.example.prabhim.dto.attendance.AttendanceMarkRequest;
import com.example.prabhim.dto.attendance.AttendanceResponse;
import com.example.prabhim.dto.attendance.AttendanceStatsResponse;
import com.example.prabhim.service.AttendanceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({"/api/v1/attendance", "/api/v1/attendance/"})
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // 1. Post / Mark Attendance
    @PostMapping({"", "/", "/mark", "/mark/"})
    public ResponseEntity<ApiResponse<AttendanceResponse>> markAttendance(
            @Valid @RequestBody AttendanceMarkRequest request
    ) {
        AttendanceResponse resp = attendanceService.markAttendance(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Attendance marked successfully", resp));
    }

    // 2. List All Attendance
    @GetMapping({"", "/"})
    public ResponseEntity<ApiResponse<Page<AttendanceResponse>>> getAllAttendance(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String creatorEmail,
            @RequestParam(required = false) String gymId,
            @RequestParam(required = false) String tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        LocalDate parsedDate = parseDateParam(date);
        Pageable pageable = PageRequest.of(page, size);
        Page<AttendanceResponse> results = attendanceService.getAttendance(search, parsedDate, status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Attendance records retrieved successfully", results));
    }

    // 3. Attendance Stats
    @GetMapping({"/stats", "/stats/"})
    public ResponseEntity<ApiResponse<AttendanceStatsResponse>> getAttendanceStats(
            @RequestParam(required = false) String date
    ) {
        LocalDate parsedDate = parseDateParam(date);
        AttendanceStatsResponse stats = attendanceService.getAttendanceStats(parsedDate);
        return ResponseEntity.ok(ApiResponse.success("Attendance stats retrieved successfully", stats));
    }

    // 4. Export CSV
    @GetMapping({"/export", "/export/"})
    public ResponseEntity<byte[]> exportAttendanceCsv(@RequestParam(required = false) String date) {
        LocalDate parsedDate = parseDateParam(date);
        byte[] csvBytes = attendanceService.exportAttendanceCsv(parsedDate);
        String filename = "PrabhimFit_Attendance_" + parsedDate + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csvBytes);
    }

    // 5. Get One Attendance Using ID
    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<AttendanceResponse>> getAttendanceById(@PathVariable UUID id) {
        AttendanceResponse resp = attendanceService.getAttendanceById(id);
        return ResponseEntity.ok(ApiResponse.success("Attendance record retrieved successfully", resp));
    }

    // 6. Quick Checkout
    @PostMapping({"/{id}/checkout", "/{id}/checkout/"})
    public ResponseEntity<ApiResponse<AttendanceResponse>> checkOut(
            @PathVariable UUID id,
            @RequestBody(required = false) AttendanceCheckOutRequest request
    ) {
        AttendanceResponse resp = attendanceService.checkOut(id, request);
        return ResponseEntity.ok(ApiResponse.success("Checked out successfully", resp));
    }

    @PutMapping({"/{id}/checkout", "/{id}/checkout/"})
    public ResponseEntity<ApiResponse<AttendanceResponse>> putCheckOut(
            @PathVariable UUID id,
            @RequestBody(required = false) AttendanceCheckOutRequest request
    ) {
        AttendanceResponse resp = attendanceService.checkOut(id, request);
        return ResponseEntity.ok(ApiResponse.success("Checked out successfully", resp));
    }

    // 7. Edit Attendance
    @PutMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<AttendanceResponse>> editAttendance(
            @PathVariable UUID id,
            @RequestBody AttendanceMarkRequest request
    ) {
        AttendanceResponse resp = attendanceService.updateAttendance(id, request);
        return ResponseEntity.ok(ApiResponse.success("Attendance updated successfully", resp));
    }

    @PatchMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<AttendanceResponse>> patchAttendance(
            @PathVariable UUID id,
            @RequestBody AttendanceMarkRequest request
    ) {
        AttendanceResponse resp = attendanceService.updateAttendance(id, request);
        return ResponseEntity.ok(ApiResponse.success("Attendance updated successfully", resp));
    }

    // 8. Delete Attendance
    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<Void>> deleteAttendance(@PathVariable UUID id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.ok(ApiResponse.success("Attendance deleted successfully", null));
    }

    private LocalDate parseDateParam(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return LocalDate.now();
        }
        String clean = dateStr.trim();
        try {
            if (clean.contains("-")) {
                String[] parts = clean.split("-");
                if (parts[0].length() == 4) {
                    return LocalDate.parse(clean, DateTimeFormatter.ISO_LOCAL_DATE);
                } else if (parts[2].length() == 4) {
                    return LocalDate.parse(clean, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                }
            }
        } catch (Exception ignored) {
        }
        return LocalDate.now();
    }
}
