package com.dksa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dksa.dto.AdminBookingResponse;
import com.dksa.dto.OfflineBookingRequest;
import com.dksa.repository.BookingRepository;
import com.dksa.service.BookingService;

@RestController
@RequestMapping("/api/admin/bookings")
public class AdminBookingController {

    private final BookingService
            bookingService;
    
    @Autowired
    private BookingRepository bookingRepository;

    public AdminBookingController(
            BookingService bookingService) {

        this.bookingService =
                bookingService;
    }

    @GetMapping
	public List<AdminBookingResponse> getAllBookings() {
		return bookingService.findAllByOrderByIdDesc();
	}
    
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String>
    cancelBooking(
            @PathVariable
            Long bookingId) {

        bookingService.cancelBooking(
                bookingId);

        return ResponseEntity.ok(
                "Booking Cancelled");
    }
    
    @PostMapping("/offline")
    public ResponseEntity<?> createOfflineBooking(
            @RequestBody OfflineBookingRequest request) {

    	bookingService.createOfflineBooking(request);

        return ResponseEntity.ok("Booking Created Successfully");
    }
}