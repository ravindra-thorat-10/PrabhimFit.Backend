package com.example.prabhim.dto.member;

import java.util.List;

public class MemberProfileResponse {

    private MemberResponse member;
    private MembershipDto membership;
    private AttendanceSummaryDto attendance;
    private TrainerSummaryDto trainer;
    private List<PaymentDto> payments;
    private List<WorkoutLogDto> workouts;
    private List<BodyMeasurementDto> measurements;
    private DietPlanDto dietPlan;
    private List<PtSessionDto> ptSessions;
    private List<MemberActivityLogDto> activityLogs;

    public MemberProfileResponse() {
    }

    public MemberProfileResponse(MemberResponse member,
                                 MembershipDto membership,
                                 AttendanceSummaryDto attendance,
                                 TrainerSummaryDto trainer,
                                 List<PaymentDto> payments,
                                 List<WorkoutLogDto> workouts,
                                 List<BodyMeasurementDto> measurements,
                                 DietPlanDto dietPlan,
                                 List<PtSessionDto> ptSessions,
                                 List<MemberActivityLogDto> activityLogs) {
        this.member = member;
        this.membership = membership;
        this.attendance = attendance;
        this.trainer = trainer;
        this.payments = payments;
        this.workouts = workouts;
        this.measurements = measurements;
        this.dietPlan = dietPlan;
        this.ptSessions = ptSessions;
        this.activityLogs = activityLogs;
    }

    public MemberResponse getMember() {
        return member;
    }

    public void setMember(MemberResponse member) {
        this.member = member;
    }

    public MembershipDto getMembership() {
        return membership;
    }

    public void setMembership(MembershipDto membership) {
        this.membership = membership;
    }

    public AttendanceSummaryDto getAttendance() {
        return attendance;
    }

    public void setAttendance(AttendanceSummaryDto attendance) {
        this.attendance = attendance;
    }

    public TrainerSummaryDto getTrainer() {
        return trainer;
    }

    public void setTrainer(TrainerSummaryDto trainer) {
        this.trainer = trainer;
    }

    public List<PaymentDto> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentDto> payments) {
        this.payments = payments;
    }

    public List<WorkoutLogDto> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<WorkoutLogDto> workouts) {
        this.workouts = workouts;
    }

    public List<BodyMeasurementDto> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<BodyMeasurementDto> measurements) {
        this.measurements = measurements;
    }

    public DietPlanDto getDietPlan() {
        return dietPlan;
    }

    public void setDietPlan(DietPlanDto dietPlan) {
        this.dietPlan = dietPlan;
    }

    public List<PtSessionDto> getPtSessions() {
        return ptSessions;
    }

    public void setPtSessions(List<PtSessionDto> ptSessions) {
        this.ptSessions = ptSessions;
    }

    public List<MemberActivityLogDto> getActivityLogs() {
        return activityLogs;
    }

    public void setActivityLogs(List<MemberActivityLogDto> activityLogs) {
        this.activityLogs = activityLogs;
    }
}
