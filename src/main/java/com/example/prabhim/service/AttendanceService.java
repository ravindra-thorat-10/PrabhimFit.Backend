package com.example.prabhim.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.prabhim.dto.attendance.AttendanceCheckOutRequest;
import com.example.prabhim.dto.attendance.AttendanceMarkRequest;
import com.example.prabhim.dto.attendance.AttendanceResponse;
import com.example.prabhim.dto.attendance.AttendanceStatsResponse;

public interface AttendanceService {

    Page<AttendanceResponse> getAttendance(String search, LocalDate date, String status, Pageable pageable);

    AttendanceResponse getAttendanceById(UUID id);

    AttendanceStatsResponse getAttendanceStats(LocalDate date);

    AttendanceResponse markAttendance(AttendanceMarkRequest request);

    AttendanceResponse updateAttendance(UUID id, AttendanceMarkRequest request);

    AttendanceResponse checkOut(UUID id, AttendanceCheckOutRequest request);

    void deleteAttendance(UUID id);

    byte[] exportAttendanceCsv(LocalDate date);

    void seedDefaultAttendanceIfEmpty();
}
