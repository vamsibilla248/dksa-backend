package com.dksa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {

    private Long totalUsers;

    private Long totalBookings;

    private Double totalRevenue;

    private Long totalPayments;
}