package com.dksa.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dksa.dto.AnalyticsResponse;
import com.dksa.dto.DashboardResponse;
import com.dksa.dto.PaymentToggleRequest;
import com.dksa.entity.Booking;
import com.dksa.entity.Payment;
import com.dksa.entity.User;
import com.dksa.repository.BookingRepository;
import com.dksa.repository.PaymentRepository;
import com.dksa.repository.UserRepository;
import com.dksa.service.AdminService;
import com.dksa.service.AppSettingService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    
    private final BookingRepository bookingRepository;
    
    private final PaymentRepository paymentRepository;
    
    private final UserRepository userRepository;
    
	private final AppSettingService appSettingService;

   

	public AdminController(
	        AdminService adminService,
	        BookingRepository bookingRepository,
	        PaymentRepository paymentRepository,
	        UserRepository userRepository,
	        AppSettingService appSettingService) {

	    this.adminService = adminService;
	    this.bookingRepository = bookingRepository;
	    this.paymentRepository = paymentRepository;
	    this.userRepository = userRepository;
	    this.appSettingService =
	            appSettingService;
	}

	@GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse>
    dashboard() {

        return ResponseEntity.ok(
                adminService.getDashboard());
    }
    
    @GetMapping("/all-bookings")
    public ResponseEntity<List<Booking>>
    getAllBookings() {

        return ResponseEntity.ok(
                bookingRepository.findAll());
    }
    
    @GetMapping("/payments")
    public ResponseEntity<List<Payment>>
    getAllPayments() {

        return ResponseEntity.ok(
                paymentRepository.findAll());
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<User>>
    getAllUsers() {

        return ResponseEntity.ok(
                userRepository.findAll());
    }
    
	@PostMapping("/payment-toggle")
	public ResponseEntity<String> togglePayment(@RequestBody PaymentToggleRequest request) {

		appSettingService.updatePaymentStatus(request.isPaymentRequired());

		return ResponseEntity.ok("Payment setting updated");
	}

	@GetMapping("/payment-toggle")
	public ResponseEntity<Boolean> getPaymentStatus() {

		return ResponseEntity.ok(appSettingService.getPaymentStatus());
	}
	
	@GetMapping("/analytics")
	public ResponseEntity<AnalyticsResponse>
	getAnalytics() {

	    return ResponseEntity.ok(
	            adminService.getAnalytics());
	}
}