package com.dksa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalyticsResponse {

    private Double totalRevenue;

    private Long totalBookings;

    private String mostPopularTurf;

    private String peakHour;

    private Long totalCustomers;
}