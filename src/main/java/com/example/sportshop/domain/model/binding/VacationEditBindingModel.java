package com.example.sportshop.domain.model.binding;

public class VacationEditBindingModel {
    private String startDate;
    private String endDate;

    public VacationEditBindingModel() {
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
