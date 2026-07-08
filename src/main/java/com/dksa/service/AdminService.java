package com.dksa.service;

import com.dksa.dto.AnalyticsResponse;
import com.dksa.dto.DashboardResponse;

public interface AdminService {

    DashboardResponse getDashboard();
    
    AnalyticsResponse getAnalytics();
}