package com.example.prabhim.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class ReportControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/reports/overview - should return 200 and gym overview report")
    void testGetGymOverview() throws Exception {
        mockMvc.perform(get("/api/v1/reports/overview")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalMembers").exists())
                .andExpect(jsonPath("$.data.activeMembers").exists())
                .andExpect(jsonPath("$.data.totalLifetimeRevenue").exists())
                .andExpect(jsonPath("$.data.totalTrainers").exists())
                .andExpect(jsonPath("$.data.planDistribution").isArray());
    }

    @Test
    @DisplayName("GET /api/v1/reports/financial - should return 200 and financial report")
    void testGetFinancialReport() throws Exception {
        mockMvc.perform(get("/api/v1/reports/financial")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalCollections").exists())
                .andExpect(jsonPath("$.data.paymentMethodBreakdown").isArray())
                .andExpect(jsonPath("$.data.statusBreakdown").isArray())
                .andExpect(jsonPath("$.data.monthlyTrends").isArray())
                .andExpect(jsonPath("$.data.recentTransactions").isArray());
    }

    @Test
    @DisplayName("GET /api/v1/reports/attendance - should return 200 and attendance report")
    void testGetAttendanceReport() throws Exception {
        mockMvc.perform(get("/api/v1/reports/attendance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalCheckIns").exists())
                .andExpect(jsonPath("$.data.dailyTrends").isArray())
                .andExpect(jsonPath("$.data.peakHours").isArray());
    }

    @Test
    @DisplayName("GET /api/v1/reports/memberships - should return 200 and membership report")
    void testGetMembershipReport() throws Exception {
        mockMvc.perform(get("/api/v1/reports/memberships")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalActiveMemberships").exists())
                .andExpect(jsonPath("$.data.planBreakdown").isArray())
                .andExpect(jsonPath("$.data.expiringSoonMembers").isArray());
    }

    @Test
    @DisplayName("GET /api/v1/reports/overview/export - should return CSV file")
    void testExportGymOverviewCsv() throws Exception {
        mockMvc.perform(get("/api/v1/reports/overview/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("gym_overview_report_")))
                .andExpect(content().contentType("text/csv; charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("PRABHIM FIT - GYM OVERVIEW & PERFORMANCE REPORT")));
    }

    @Test
    @DisplayName("GET /api/v1/reports/financial/export - should return CSV file")
    void testExportFinancialCsv() throws Exception {
        mockMvc.perform(get("/api/v1/reports/financial/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("financial_report_")))
                .andExpect(content().contentType("text/csv; charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Invoice Number,Payment Date,Member Code")));
    }

    @Test
    @DisplayName("GET /api/v1/reports/attendance/export - should return CSV file")
    void testExportAttendanceCsv() throws Exception {
        mockMvc.perform(get("/api/v1/reports/attendance/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("attendance_report_")))
                .andExpect(content().contentType("text/csv; charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Date,Check-in Time,Check-out Time")));
    }

    @Test
    @DisplayName("GET /api/v1/reports/memberships/export - should return CSV file")
    void testExportMembershipCsv() throws Exception {
        mockMvc.perform(get("/api/v1/reports/memberships/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("membership_report_")))
                .andExpect(content().contentType("text/csv; charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Member Code,Member Name,Plan Name")));
    }
}
