package com.example.prabhim.dto.diet;

public class DietStatsResponse {

    private Long totalProtocols;
    private Long activeProtocols;
    private Long inactiveProtocols;
    private Double averageCalories;
    private Double averageWaterTarget;

    public DietStatsResponse() {
    }

    public DietStatsResponse(Long totalProtocols, Long activeProtocols, Long inactiveProtocols,
                             Double averageCalories, Double averageWaterTarget) {
        this.totalProtocols = totalProtocols;
        this.activeProtocols = activeProtocols;
        this.inactiveProtocols = inactiveProtocols;
        this.averageCalories = averageCalories;
        this.averageWaterTarget = averageWaterTarget;
    }

    public Long getTotalProtocols() {
        return totalProtocols;
    }

    public void setTotalProtocols(Long totalProtocols) {
        this.totalProtocols = totalProtocols;
    }

    public Long getActiveProtocols() {
        return activeProtocols;
    }

    public void setActiveProtocols(Long activeProtocols) {
        this.activeProtocols = activeProtocols;
    }

    public Long getInactiveProtocols() {
        return inactiveProtocols;
    }

    public void setInactiveProtocols(Long inactiveProtocols) {
        this.inactiveProtocols = inactiveProtocols;
    }

    public Double getAverageCalories() {
        return averageCalories;
    }

    public void setAverageCalories(Double averageCalories) {
        this.averageCalories = averageCalories;
    }

    public Double getAverageWaterTarget() {
        return averageWaterTarget;
    }

    public void setAverageWaterTarget(Double averageWaterTarget) {
        this.averageWaterTarget = averageWaterTarget;
    }
}
