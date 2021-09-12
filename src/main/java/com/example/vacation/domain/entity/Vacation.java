package com.example.vacation.domain.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "vacations")
public class Vacation extends BaseEntity{
    private String startDate;
    private String endDate;
    private String username;


    public Vacation() {
    }

    public Vacation(String startDate, String endDate, String username) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
