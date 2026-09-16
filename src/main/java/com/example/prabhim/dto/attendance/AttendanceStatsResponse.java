package com.example.prabhim.dto.attendance;

import java.time.LocalDate;

public class AttendanceStatsResponse {

    private long onTimeCheckInsToday;
    private long lateArrivalsToday;
    private long totalRegisteredAthletes;
    private long currentlyInGym;
    private LocalDate selectedDate;

    public AttendanceStatsResponse() {
    }

    public long getOnTimeCheckInsToday() {
        return onTimeCheckInsToday;
    }

    public void setOnTimeCheckInsToday(long onTimeCheckInsToday) {
        this.onTimeCheckInsToday = onTimeCheckInsToday;
    }

    public long getLateArrivalsToday() {
        return lateArrivalsToday;
    }

    public void setLateArrivalsToday(long lateArrivalsToday) {
        this.lateArrivalsToday = lateArrivalsToday;
    }

    public long getTotalRegisteredAthletes() {
        return totalRegisteredAthletes;
    }

    public void setTotalRegisteredAthletes(long totalRegisteredAthletes) {
        this.totalRegisteredAthletes = totalRegisteredAthletes;
    }

    public long getCurrentlyInGym() {
        return currentlyInGym;
    }

    public void setCurrentlyInGym(long currentlyInGym) {
        this.currentlyInGym = currentlyInGym;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }
}
