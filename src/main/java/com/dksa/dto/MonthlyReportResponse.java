package com.dksa.dto;

import lombok.Data;

@Data
public class MonthlyReportResponse {

    private String month;

    private Double revenue;

    private Long bookings;
}