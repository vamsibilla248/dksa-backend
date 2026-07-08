package com.dksa.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dksa.dto.TurfResponse;
import com.dksa.service.TurfService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

	private final TurfService turfService;

	public CustomerController(TurfService turfService) {

		this.turfService = turfService;
	}

	@GetMapping("/dashboard")
	public String customerDashboard() {

		return "Welcome Customer";
	}

	@GetMapping("/turfs")
	public ResponseEntity<List<TurfResponse>> getActiveTurfs() {

		return ResponseEntity.ok(turfService.getActiveTurfs());
	}
}