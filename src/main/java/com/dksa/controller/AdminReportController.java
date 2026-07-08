package com.dksa.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dksa.dto.MonthlyReportResponse;
import com.dksa.dto.ReportResponse;
import com.dksa.service.BookingService;

@RestController
@RequestMapping("/api/admin/reports")
public class AdminReportController {

	private final BookingService bookingService;

	public AdminReportController(BookingService bookingService) {

		this.bookingService = bookingService;
	}

	@GetMapping
	public ResponseEntity<ReportResponse> getReports() {

		return ResponseEntity.ok(bookingService.getReports());
	}
	
	@GetMapping("/monthly")
	public ResponseEntity<
	        List<MonthlyReportResponse>>
	getMonthlyReports() {

	    return ResponseEntity.ok(
	            bookingService
	                    .getMonthlyReports());
	}
	
	
}