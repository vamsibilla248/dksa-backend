package com.dksa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportResponse {

    private Long totalBookings;

    private Double totalRevenue;

    private Long todayBookings;

    private Double todayRevenue;
}