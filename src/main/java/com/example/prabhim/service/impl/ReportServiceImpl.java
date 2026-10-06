package com.example.prabhim.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prabhim.dto.payment.PaymentResponse;
import com.example.prabhim.dto.report.AttendanceReportResponse;
import com.example.prabhim.dto.report.FinancialReportResponse;
import com.example.prabhim.dto.report.GymOverviewReportResponse;
import com.example.prabhim.dto.report.MembershipReportResponse;
import com.example.prabhim.entity.Attendance;
import com.example.prabhim.entity.Membership;
import com.example.prabhim.entity.MembershipPlan;
import com.example.prabhim.entity.Payment;
import com.example.prabhim.entity.enums.MemberStatus;
import com.example.prabhim.entity.enums.MembershipStatus;
import com.example.prabhim.entity.enums.PaymentStatus;
import com.example.prabhim.repository.AttendanceRepository;
import com.example.prabhim.repository.MemberRepository;
import com.example.prabhim.repository.MembershipPlanRepository;
import com.example.prabhim.repository.MembershipRepository;
import com.example.prabhim.repository.PaymentRepository;
import com.example.prabhim.repository.TrainerRepository;
import com.example.prabhim.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;
    private final AttendanceRepository attendanceRepository;
    private final TrainerRepository trainerRepository;
    private final MembershipRepository membershipRepository;
    private final MembershipPlanRepository membershipPlanRepository;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter MONTH_FMT = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);

    public ReportServiceImpl(MemberRepository memberRepository,
                             PaymentRepository paymentRepository,
                             AttendanceRepository attendanceRepository,
                             TrainerRepository trainerRepository,
                             MembershipRepository membershipRepository,
                             MembershipPlanRepository membershipPlanRepository) {
        this.memberRepository = memberRepository;
        this.paymentRepository = paymentRepository;
        this.attendanceRepository = attendanceRepository;
        this.trainerRepository = trainerRepository;
        this.membershipRepository = membershipRepository;
        this.membershipPlanRepository = membershipPlanRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public GymOverviewReportResponse getGymOverviewReport() {
        GymOverviewReportResponse resp = new GymOverviewReportResponse();

        // 1. Member KPIs
        long totalMembers = memberRepository.count();
        long activeMembers = memberRepository.countByStatus(MemberStatus.ACTIVE);
        long inactiveMembers = memberRepository.countByStatus(MemberStatus.INACTIVE);
        long expiredMembers = memberRepository.countByStatus(MemberStatus.EXPIRED);

        YearMonth currentYearMonth = YearMonth.now();
        LocalDate startOfMonth = currentYearMonth.atDay(1);
        LocalDate endOfMonth = currentYearMonth.atEndOfMonth();
        long newMembersThisMonth = memberRepository.countByJoinDateBetween(startOfMonth, endOfMonth);

        double retentionRate = totalMembers > 0
                ? BigDecimal.valueOf((double) activeMembers / totalMembers * 100).setScale(2, RoundingMode.HALF_UP).doubleValue()
                : 100.0;

        resp.setTotalMembers(totalMembers);
        resp.setActiveMembers(activeMembers);
        resp.setInactiveMembers(inactiveMembers);
        resp.setExpiredMembers(expiredMembers);
        resp.setNewMembersThisMonth(newMembersThisMonth);
        resp.setRetentionRatePercentage(retentionRate);

        // 2. Revenue KPIs
        BigDecimal lifetimeRevenue = paymentRepository.findTotalLifetimeCollections();
        if (lifetimeRevenue == null) lifetimeRevenue = BigDecimal.ZERO;

        LocalDateTime monthStart = startOfMonth.atStartOfDay();
        LocalDateTime monthEnd = endOfMonth.atTime(LocalTime.MAX);
        BigDecimal thisMonthRevenue = paymentRepository.findRevenueBetween(monthStart, monthEnd);
        if (thisMonthRevenue == null) thisMonthRevenue = BigDecimal.ZERO;

        BigDecimal pendingDues = paymentRepository.findPendingUncollectedDues();
        if (pendingDues == null) pendingDues = BigDecimal.ZERO;

        long paidCount = paymentRepository.countByStatus(PaymentStatus.PAID);
        long pendingCount = paymentRepository.countByStatus(PaymentStatus.PENDING);

        resp.setTotalLifetimeRevenue(lifetimeRevenue);
        resp.setThisMonthRevenue(thisMonthRevenue);
        resp.setPendingDues(pendingDues);
        resp.setPaidInvoicesCount(paidCount);
        resp.setPendingInvoicesCount(pendingCount);

        // 3. Attendance KPIs
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);
        long todayCheckIns = attendanceRepository.countTotalCheckInsBetween(todayStart, todayEnd);
        long thisMonthCheckIns = attendanceRepository.countByCheckInTimeBetween(monthStart, monthEnd);
        long onTimeToday = attendanceRepository.countOnTimeCheckInsBetween(todayStart, todayEnd);
        long lateToday = attendanceRepository.countLateArrivalsBetween(todayStart, todayEnd);

        resp.setTodayCheckIns(todayCheckIns);
        resp.setThisMonthCheckIns(thisMonthCheckIns);
        resp.setOnTimeCheckInsToday(onTimeToday);
        resp.setLateCheckInsToday(lateToday);

        // 4. Trainer KPIs
        long totalTrainers = trainerRepository.count();
        long activeTrainers = trainerRepository.countByStatusIgnoreCase("ACTIVE");
        BigDecimal monthlyPayroll = trainerRepository.sumActiveMonthlySalary();
        if (monthlyPayroll == null) monthlyPayroll = BigDecimal.ZERO;

        double avgMembersPerTrainer = activeTrainers > 0
                ? BigDecimal.valueOf((double) activeMembers / activeTrainers).setScale(1, RoundingMode.HALF_UP).doubleValue()
                : 0.0;

        resp.setTotalTrainers(totalTrainers);
        resp.setActiveTrainers(activeTrainers);
        resp.setTotalMonthlyPayroll(monthlyPayroll);
        resp.setAverageMembersPerTrainer(avgMembersPerTrainer);

        // 5. Membership Plan Distribution
        List<Membership> activeMemberships = membershipRepository.findByStatus(MembershipStatus.ACTIVE);
        Map<String, Long> planCountMap = activeMemberships.stream()
                .filter(m -> m.getPlanName() != null && !m.getPlanName().trim().isEmpty())
                .collect(Collectors.groupingBy(Membership::getPlanName, Collectors.counting()));

        long totalActivePlans = activeMemberships.size();
        List<GymOverviewReportResponse.PlanDistributionItem> planItems = new ArrayList<>();
        for (Map.Entry<String, Long> entry : planCountMap.entrySet()) {
            double pct = totalActivePlans > 0
                    ? BigDecimal.valueOf((double) entry.getValue() / totalActivePlans * 100).setScale(2, RoundingMode.HALF_UP).doubleValue()
                    : 0.0;
            planItems.add(new GymOverviewReportResponse.PlanDistributionItem(entry.getKey(), entry.getValue(), pct));
        }
        planItems.sort(Comparator.comparing(GymOverviewReportResponse.PlanDistributionItem::getMemberCount).reversed());
        resp.setPlanDistribution(planItems);

        return resp;
    }

    @Override
    @Transactional(readOnly = true)
    public FinancialReportResponse getFinancialReport(LocalDate startDate, LocalDate endDate, PaymentStatus status, String paymentMethod) {
        FinancialReportResponse resp = new FinancialReportResponse();

        List<Payment> allPayments;
        if (startDate != null && endDate != null) {
            allPayments = paymentRepository.findByPaymentDateBetweenOrderByPaymentDateDesc(
                    startDate.atStartOfDay(),
                    endDate.atTime(LocalTime.MAX)
            );
        } else {
            allPayments = paymentRepository.findAllByOrderByPaymentDateDesc();
        }

        // Apply filters if provided
        List<Payment> filteredPayments = allPayments.stream()
                .filter(p -> status == null || p.getStatus() == status)
                .filter(p -> paymentMethod == null || paymentMethod.trim().isEmpty()
                        || (p.getPaymentMethod() != null && p.getPaymentMethod().equalsIgnoreCase(paymentMethod.trim())))
                .collect(Collectors.toList());

        BigDecimal totalCollections = BigDecimal.ZERO;
        BigDecimal pendingDues = BigDecimal.ZERO;
        BigDecimal refundedAmount = BigDecimal.ZERO;
        BigDecimal failedAmount = BigDecimal.ZERO;

        Map<String, BigDecimal> methodAmountMap = new HashMap<>();
        Map<String, Long> methodCountMap = new HashMap<>();

        Map<PaymentStatus, BigDecimal> statusAmountMap = new HashMap<>();
        Map<PaymentStatus, Long> statusCountMap = new HashMap<>();

        Map<YearMonth, BigDecimal> monthlyAmountMap = new TreeMap<>();
        Map<YearMonth, Long> monthlyCountMap = new TreeMap<>();

        for (Payment p : filteredPayments) {
            BigDecimal amt = p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO;
            PaymentStatus st = p.getStatus() != null ? p.getStatus() : PaymentStatus.PAID;

            // Status aggregation
            statusAmountMap.put(st, statusAmountMap.getOrDefault(st, BigDecimal.ZERO).add(amt));
            statusCountMap.put(st, statusCountMap.getOrDefault(st, 0L) + 1);

            if (st == PaymentStatus.PAID) {
                totalCollections = totalCollections.add(amt);

                // Method breakdown (only for paid)
                String method = p.getPaymentMethod() != null && !p.getPaymentMethod().trim().isEmpty()
                        ? p.getPaymentMethod().toUpperCase()
                        : "OTHER";
                methodAmountMap.put(method, methodAmountMap.getOrDefault(method, BigDecimal.ZERO).add(amt));
                methodCountMap.put(method, methodCountMap.getOrDefault(method, 0L) + 1);

                // Monthly trends
                if (p.getPaymentDate() != null) {
                    YearMonth ym = YearMonth.from(p.getPaymentDate());
                    monthlyAmountMap.put(ym, monthlyAmountMap.getOrDefault(ym, BigDecimal.ZERO).add(amt));
                    monthlyCountMap.put(ym, monthlyCountMap.getOrDefault(ym, 0L) + 1);
                }
            } else if (st == PaymentStatus.PENDING) {
                pendingDues = pendingDues.add(amt);
            } else if (st == PaymentStatus.REFUNDED) {
                refundedAmount = refundedAmount.add(amt);
            } else if (st == PaymentStatus.FAILED) {
                failedAmount = failedAmount.add(amt);
            }
        }

        long totalTx = filteredPayments.size();
        BigDecimal avgTxValue = totalTx > 0 && totalCollections.compareTo(BigDecimal.ZERO) > 0
                ? totalCollections.divide(BigDecimal.valueOf(statusCountMap.getOrDefault(PaymentStatus.PAID, 1L)), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        resp.setTotalCollections(totalCollections);
        resp.setPendingDues(pendingDues);
        resp.setRefundedAmount(refundedAmount);
        resp.setFailedAmount(failedAmount);
        resp.setTotalTransactions(totalTx);
        resp.setAverageTransactionValue(avgTxValue);

        // Payment Method Breakdown
        List<FinancialReportResponse.PaymentMethodMetric> methodList = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : methodAmountMap.entrySet()) {
            double pct = totalCollections.compareTo(BigDecimal.ZERO) > 0
                    ? entry.getValue().divide(totalCollections, 4, RoundingMode.HALF_UP).doubleValue() * 100
                    : 0.0;
            methodList.add(new FinancialReportResponse.PaymentMethodMetric(
                    entry.getKey(),
                    entry.getValue(),
                    methodCountMap.getOrDefault(entry.getKey(), 0L),
                    pct
            ));
        }
        methodList.sort(Comparator.comparing(FinancialReportResponse.PaymentMethodMetric::getAmount).reversed());
        resp.setPaymentMethodBreakdown(methodList);

        // Status Breakdown
        List<FinancialReportResponse.PaymentStatusMetric> statusList = new ArrayList<>();
        for (PaymentStatus st : PaymentStatus.values()) {
            BigDecimal amt = statusAmountMap.getOrDefault(st, BigDecimal.ZERO);
            Long count = statusCountMap.getOrDefault(st, 0L);
            if (count > 0 || amt.compareTo(BigDecimal.ZERO) > 0) {
                statusList.add(new FinancialReportResponse.PaymentStatusMetric(st.name(), amt, count));
            }
        }
        resp.setStatusBreakdown(statusList);

        // Monthly Trends
        List<FinancialReportResponse.MonthlyRevenueMetric> trends = new ArrayList<>();
        for (Map.Entry<YearMonth, BigDecimal> entry : monthlyAmountMap.entrySet()) {
            String period = entry.getKey().toString();
            String label = entry.getKey().format(MONTH_FMT);
            trends.add(new FinancialReportResponse.MonthlyRevenueMetric(
                    period,
                    label,
                    entry.getValue(),
                    monthlyCountMap.getOrDefault(entry.getKey(), 0L)
            ));
        }
        resp.setMonthlyTrends(trends);

        // Recent 20 Transactions
        List<PaymentResponse> recentTx = filteredPayments.stream()
                .limit(20)
                .map(PaymentResponse::fromEntity)
                .collect(Collectors.toList());
        resp.setRecentTransactions(recentTx);

        return resp;
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceReportResponse getAttendanceReport(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(29);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        LocalDateTime startDt = startDate.atStartOfDay();
        LocalDateTime endDt = endDate.atTime(LocalTime.MAX);

        List<Attendance> attendances = attendanceRepository.findByCheckInTimeBetweenOrderByCheckInTimeDesc(startDt, endDt);

        AttendanceReportResponse resp = new AttendanceReportResponse();
        long total = attendances.size();
        long onTime = attendances.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus())).count();
        long late = attendances.stream().filter(a -> "LATE".equalsIgnoreCase(a.getStatus())).count();

        long daysCount = Math.max(1, ChronoUnit.DAYS.between(startDate, endDate) + 1);
        double avgDaily = BigDecimal.valueOf((double) total / daysCount).setScale(1, RoundingMode.HALF_UP).doubleValue();

        resp.setTotalCheckIns(total);
        resp.setOnTimeCheckIns(onTime);
        resp.setLateCheckIns(late);
        resp.setAverageDailyCheckIns(avgDaily);

        // Daily breakdown
        Map<LocalDate, List<Attendance>> dailyMap = attendances.stream()
                .collect(Collectors.groupingBy(a -> a.getCheckInTime().toLocalDate()));

        List<AttendanceReportResponse.DailyAttendanceMetric> dailyTrends = new ArrayList<>();
        for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
            List<Attendance> dayList = dailyMap.getOrDefault(d, List.of());
            long dayTotal = dayList.size();
            long dayOnTime = dayList.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus())).count();
            long dayLate = dayList.stream().filter(a -> "LATE".equalsIgnoreCase(a.getStatus())).count();
            String dayOfWeek = d.getDayOfWeek().name().substring(0, 3);
            dailyTrends.add(new AttendanceReportResponse.DailyAttendanceMetric(d, dayOfWeek, dayTotal, dayOnTime, dayLate));
        }
        resp.setDailyTrends(dailyTrends);

        // Peak Hours Breakdown (Hourly bins)
        Map<Integer, Long> hourMap = attendances.stream()
                .collect(Collectors.groupingBy(a -> a.getCheckInTime().getHour(), Collectors.counting()));

        List<AttendanceReportResponse.HourlyAttendanceMetric> peakHours = new ArrayList<>();
        for (int h = 5; h <= 22; h++) {
            String timeSlot = String.format("%02d:00 - %02d:00", h, h + 1);
            peakHours.add(new AttendanceReportResponse.HourlyAttendanceMetric(timeSlot, hourMap.getOrDefault(h, 0L)));
        }
        resp.setPeakHours(peakHours);

        return resp;
    }

    @Override
    @Transactional(readOnly = true)
    public MembershipReportResponse getMembershipReport() {
        MembershipReportResponse resp = new MembershipReportResponse();

        long activeCount = membershipRepository.countByStatus(MembershipStatus.ACTIVE);
        long expiredCount = membershipRepository.countByStatus(MembershipStatus.EXPIRED);
        long expiring7Days = membershipRepository.countExpiringBetween(LocalDate.now(), LocalDate.now().plusDays(7));
        long expiring30Days = membershipRepository.countExpiringBetween(LocalDate.now(), LocalDate.now().plusDays(30));

        resp.setTotalActiveMemberships(activeCount);
        resp.setTotalExpiredMemberships(expiredCount);
        resp.setExpiringWithin7Days(expiring7Days);
        resp.setExpiringWithin30Days(expiring30Days);

        // Plan Breakdown
        List<MembershipPlan> plans = membershipPlanRepository.findAll();
        List<Membership> activeMemberships = membershipRepository.findByStatus(MembershipStatus.ACTIVE);

        Map<String, List<Membership>> planGroupMap = activeMemberships.stream()
                .filter(m -> m.getPlanName() != null)
                .collect(Collectors.groupingBy(Membership::getPlanName));

        List<MembershipReportResponse.PlanBreakdownMetric> planMetrics = new ArrayList<>();
        for (MembershipPlan plan : plans) {
            List<Membership> mList = planGroupMap.getOrDefault(plan.getName(), List.of());
            long count = mList.size();
            BigDecimal revenue = plan.getPrice() != null ? plan.getPrice().multiply(BigDecimal.valueOf(count)) : BigDecimal.ZERO;
            planMetrics.add(new MembershipReportResponse.PlanBreakdownMetric(
                    plan.getName(),
                    plan.getDurationMonths() != null ? plan.getDurationMonths() + " Months" : "STANDARD",
                    count,
                    revenue
            ));
        }
        planMetrics.sort(Comparator.comparing(MembershipReportResponse.PlanBreakdownMetric::getActiveCount).reversed());
        resp.setPlanBreakdown(planMetrics);

        // Expiring Soon Members (next 30 days)
        List<Membership> expiringSoon = membershipRepository.findByEndDateBetweenOrderByEndDateAsc(
                LocalDate.now(),
                LocalDate.now().plusDays(30)
        );

        List<MembershipReportResponse.ExpiringMembershipItem> expiringSoonItems = expiringSoon.stream()
                .map(m -> {
                    String memberCode = m.getMember() != null ? m.getMember().getMemberCode() : "N/A";
                    String memberName = m.getMember() != null
                            ? ((m.getMember().getFirstName() != null ? m.getMember().getFirstName() : "") + " "
                            + (m.getMember().getLastName() != null ? m.getMember().getLastName() : "")).trim()
                            : "N/A";
                    String phone = m.getMember() != null ? m.getMember().getPhone() : "N/A";
                    long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), m.getEndDate());
                    return new MembershipReportResponse.ExpiringMembershipItem(
                            memberCode,
                            memberName,
                            m.getPlanName(),
                            m.getEndDate(),
                            daysRemaining,
                            phone
                    );
                })
                .collect(Collectors.toList());
        resp.setExpiringSoonMembers(expiringSoonItems);

        return resp;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportGymOverviewCsv() {
        GymOverviewReportResponse overview = getGymOverviewReport();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(baos, true, StandardCharsets.UTF_8)) {
            writer.println("PRABHIM FIT - GYM OVERVIEW & PERFORMANCE REPORT");
            writer.println("Generated Date:," + LocalDate.now().format(DATE_FMT));
            writer.println();
            writer.println("--- MEMBERSHIP METRICS ---");
            writer.println("Metric,Value");
            writer.println("Total Members," + overview.getTotalMembers());
            writer.println("Active Members," + overview.getActiveMembers());
            writer.println("Inactive Members," + overview.getInactiveMembers());
            writer.println("Expired Members," + overview.getExpiredMembers());
            writer.println("New Members (This Month)," + overview.getNewMembersThisMonth());
            writer.println("Retention Rate," + overview.getRetentionRatePercentage() + "%");
            writer.println();
            writer.println("--- FINANCIAL & REVENUE METRICS ---");
            writer.println("Metric,Value");
            writer.println("Lifetime Revenue," + overview.getTotalLifetimeRevenueDisplay());
            writer.println("This Month Revenue," + overview.getThisMonthRevenueDisplay());
            writer.println("Pending Uncollected Dues," + overview.getPendingDuesDisplay());
            writer.println("Paid Invoices Count," + overview.getPaidInvoicesCount());
            writer.println("Pending Invoices Count," + overview.getPendingInvoicesCount());
            writer.println();
            writer.println("--- ATTENDANCE & TRAINER METRICS ---");
            writer.println("Metric,Value");
            writer.println("Today Check-ins," + overview.getTodayCheckIns());
            writer.println("This Month Check-ins," + overview.getThisMonthCheckIns());
            writer.println("On-time Check-ins Today," + overview.getOnTimeCheckInsToday());
            writer.println("Late Arrivals Today," + overview.getLateCheckInsToday());
            writer.println("Total Trainers," + overview.getTotalTrainers());
            writer.println("Active Trainers," + overview.getActiveTrainers());
            writer.println("Monthly Trainer Payroll," + overview.getTotalMonthlyPayrollDisplay());
            writer.println("Avg Members Per Trainer," + overview.getAverageMembersPerTrainer());
            writer.println();
            writer.println("--- MEMBERSHIP PLAN DISTRIBUTION ---");
            writer.println("Plan Name,Active Members Count,Percentage");
            if (overview.getPlanDistribution() != null) {
                for (GymOverviewReportResponse.PlanDistributionItem item : overview.getPlanDistribution()) {
                    writer.printf("\"%s\",%d,%.2f%%\n", escapeCsv(item.getPlanName()), item.getMemberCount(), item.getPercentage());
                }
            }
        }
        return baos.toByteArray();
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportFinancialReportCsv(LocalDate startDate, LocalDate endDate, PaymentStatus status, String paymentMethod) {
        List<Payment> allPayments;
        if (startDate != null && endDate != null) {
            allPayments = paymentRepository.findByPaymentDateBetweenOrderByPaymentDateDesc(
                    startDate.atStartOfDay(),
                    endDate.atTime(LocalTime.MAX)
            );
        } else {
            allPayments = paymentRepository.findAllByOrderByPaymentDateDesc();
        }

        List<Payment> filteredPayments = allPayments.stream()
                .filter(p -> status == null || p.getStatus() == status)
                .filter(p -> paymentMethod == null || paymentMethod.trim().isEmpty()
                        || (p.getPaymentMethod() != null && p.getPaymentMethod().equalsIgnoreCase(paymentMethod.trim())))
                .collect(Collectors.toList());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(baos, true, StandardCharsets.UTF_8)) {
            writer.println("Invoice Number,Payment Date,Member Code,Member Name,Plan Name,Payment Method,Amount,Status,Transaction Ref,Description");
            for (Payment p : filteredPayments) {
                String memberCode = p.getMember() != null ? p.getMember().getMemberCode() : (p.getMemberCode() != null ? p.getMemberCode() : "N/A");
                String memberName = p.getMember() != null
                        ? ((p.getMember().getFirstName() != null ? p.getMember().getFirstName() : "") + " "
                        + (p.getMember().getLastName() != null ? p.getMember().getLastName() : "")).trim()
                        : (p.getMemberName() != null ? p.getMemberName() : "N/A");
                String planName = p.getMembershipPlan() != null ? p.getMembershipPlan().getName() : (p.getPlanName() != null ? p.getPlanName() : "N/A");
                String dateStr = p.getPaymentDate() != null ? p.getPaymentDate().format(DATE_FMT) : "N/A";
                BigDecimal amt = p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO;

                writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%.2f,\"%s\",\"%s\",\"%s\"\n",
                        escapeCsv(p.getInvoiceNumber()),
                        dateStr,
                        escapeCsv(memberCode),
                        escapeCsv(memberName),
                        escapeCsv(planName),
                        escapeCsv(p.getPaymentMethod() != null ? p.getPaymentMethod() : "OTHER"),
                        amt,
                        p.getStatus() != null ? p.getStatus().name() : "PAID",
                        escapeCsv(p.getTransactionReference()),
                        escapeCsv(p.getDescription())
                );
            }
        }
        return baos.toByteArray();
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportAttendanceReportCsv(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) startDate = LocalDate.now().minusDays(29);
        if (endDate == null) endDate = LocalDate.now();

        List<Attendance> attendances = attendanceRepository.findByCheckInTimeBetweenOrderByCheckInTimeDesc(
                startDate.atStartOfDay(),
                endDate.atTime(LocalTime.MAX)
        );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(baos, true, StandardCharsets.UTF_8)) {
            writer.println("Date,Check-in Time,Check-out Time,Member Code,Member Name,Status,Facility,Verified By,Method");
            for (Attendance a : attendances) {
                String memberCode = a.getMember() != null ? a.getMember().getMemberCode() : "N/A";
                String memberName = a.getMember() != null
                        ? ((a.getMember().getFirstName() != null ? a.getMember().getFirstName() : "") + " "
                        + (a.getMember().getLastName() != null ? a.getMember().getLastName() : "")).trim()
                        : "N/A";
                String dateStr = a.getCheckInTime() != null ? a.getCheckInTime().format(DATE_FMT) : "N/A";
                String checkInStr = a.getCheckInTime() != null ? a.getCheckInTime().format(TIME_FMT) : "N/A";
                String checkOutStr = a.getCheckOutTime() != null ? a.getCheckOutTime().format(TIME_FMT) : "-";

                writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                        dateStr,
                        checkInStr,
                        checkOutStr,
                        escapeCsv(memberCode),
                        escapeCsv(memberName),
                        escapeCsv(a.getStatus()),
                        escapeCsv(a.getFacility()),
                        escapeCsv(a.getVerifiedBy()),
                        escapeCsv(a.getMethod())
                );
            }
        }
        return baos.toByteArray();
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportMembershipReportCsv() {
        List<Membership> memberships = membershipRepository.findAll();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(baos, true, StandardCharsets.UTF_8)) {
            writer.println("Member Code,Member Name,Plan Name,Plan Type,Start Date,End Date,Price,Status,Auto Renew");
            for (Membership m : memberships) {
                String memberCode = m.getMember() != null ? m.getMember().getMemberCode() : "N/A";
                String memberName = m.getMember() != null
                        ? ((m.getMember().getFirstName() != null ? m.getMember().getFirstName() : "") + " "
                        + (m.getMember().getLastName() != null ? m.getMember().getLastName() : "")).trim()
                        : "N/A";
                String startStr = m.getStartDate() != null ? m.getStartDate().format(DATE_FMT) : "N/A";
                String endStr = m.getEndDate() != null ? m.getEndDate().format(DATE_FMT) : "N/A";
                BigDecimal price = m.getPrice() != null ? m.getPrice() : BigDecimal.ZERO;

                writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%.2f,\"%s\",%b\n",
                        escapeCsv(memberCode),
                        escapeCsv(memberName),
                        escapeCsv(m.getPlanName()),
                        escapeCsv(m.getPlanType()),
                        startStr,
                        endStr,
                        price,
                        m.getStatus() != null ? m.getStatus().name() : "ACTIVE",
                        Boolean.TRUE.equals(m.getAutoRenew())
                );
            }
        }
        return baos.toByteArray();
    }

    private String escapeCsv(String val) {
        if (val == null) return "";
        return val.replace("\"", "\"\"");
    }
}
