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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @DisplayName("GET /api/v1/reports - should return 200 and studio analytics matching frontend ReportsPage")
    void testGetStudioAnalytics_GET() throws Exception {
        mockMvc.perform(get("/api/v1/reports")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.members.total").exists())
                .andExpect(jsonPath("$.data.members.active").exists())
                .andExpect(jsonPath("$.data.members.expired").exists())
                .andExpect(jsonPath("$.data.members.pending").exists())
                .andExpect(jsonPath("$.data.members.retentionRate").exists())
                .andExpect(jsonPath("$.data.revenue.total").exists())
                .andExpect(jsonPath("$.data.revenue.monthlyTrend").isArray())
                .andExpect(jsonPath("$.data.attendance.averageDaily").exists())
                .andExpect(jsonPath("$.data.attendance.dailyTrend").isArray())
                .andExpect(jsonPath("$.data.trainers").isArray());
    }

    @Test
    @DisplayName("GET /api/v1/reports?startDate=...&endDate=... - should return 200 with date range filtering")
    void testGetStudioAnalytics_WithDateRange() throws Exception {
        mockMvc.perform(get("/api/v1/reports")
                        .param("startDate", "2026-01-01")
                        .param("endDate", "2026-12-31")
                        .param("status", "PAID")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.revenue.total").exists());
    }

    @Test
    @DisplayName("POST /api/v1/reports/filter - should return 200 with request body filter")
    void testFilterStudioAnalytics_POST() throws Exception {
        String requestJson = "{\"startDate\":\"2026-01-01\",\"endDate\":\"2026-12-31\",\"status\":\"PAID\",\"paymentMethod\":\"UPI\"}";
        mockMvc.perform(post("/api/v1/reports/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.members").exists())
                .andExpect(jsonPath("$.data.revenue").exists())
                .andExpect(jsonPath("$.data.attendance").exists());
    }

    @Test
    @DisplayName("POST /api/v1/reports - should accept root POST request with filter body")
    void testRootStudioAnalytics_POST() throws Exception {
        String requestJson = "{\"startDate\":\"2026-06-01\",\"endDate\":\"2026-10-15\"}";
        mockMvc.perform(post("/api/v1/reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.members").exists());
    }

    @Test
    @DisplayName("GET /api/v1/reports/export/members - should return members CSV formatted for frontend")
    void testExportMembersCsv() throws Exception {
        mockMvc.perform(get("/api/v1/reports/export/members"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("PrabhimFit_Members_Report_")))
                .andExpect(content().contentType("text/csv; charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Member Code,Full Name,Status,Plan,Phone,Join Date")));
    }

    @Test
    @DisplayName("GET /api/v1/reports/export/payments - should return payments CSV formatted for frontend")
    void testExportPaymentsCsv() throws Exception {
        mockMvc.perform(get("/api/v1/reports/export/payments"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("PrabhimFit_Payments_Report_")))
                .andExpect(content().contentType("text/csv; charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Invoice No,Member Name,Amount (INR),Payment Method,Status,Date")));
    }

    @Test
    @DisplayName("GET /api/v1/reports/export/trainers - should return trainers CSV formatted for frontend")
    void testExportTrainersCsv() throws Exception {
        mockMvc.perform(get("/api/v1/reports/export/trainers"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("PrabhimFit_Trainers_Report_")))
                .andExpect(content().contentType("text/csv; charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Trainer ID,Name,Specialization,Assigned Members,Monthly Salary (INR)")));
    }

    @Test
    @DisplayName("GET /api/v1/reports/export/attendance - should return attendance CSV formatted for frontend")
    void testExportAttendanceCsvPath() throws Exception {
        mockMvc.perform(get("/api/v1/reports/export/attendance"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("PrabhimFit_Attendance_Report_")))
                .andExpect(content().contentType("text/csv; charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Log ID,Member Name,Check-In Timestamp")));
    }

    @Test
    @DisplayName("POST /api/v1/reports/export - should return CSV via POST request body")
    void testExportReportByPost() throws Exception {
        String requestJson = "{\"type\":\"members\"}";
        mockMvc.perform(post("/api/v1/reports/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("PrabhimFit_Members_Report_")))
                .andExpect(content().contentType("text/csv; charset=UTF-8"));
    }

    @Test
    @DisplayName("POST /api/v1/reports/export/{type} - should return CSV via POST with path and body")
    void testExportReportByPost_WithPath() throws Exception {
        String requestJson = "{\"startDate\":\"2026-01-01\",\"endDate\":\"2026-12-31\"}";
        mockMvc.perform(post("/api/v1/reports/export/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("PrabhimFit_Payments_Report_")))
                .andExpect(content().contentType("text/csv; charset=UTF-8"));
    }
}
