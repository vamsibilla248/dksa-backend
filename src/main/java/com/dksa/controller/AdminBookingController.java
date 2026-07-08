package com.dksa.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dksa.dto.AdminBookingResponse;
import com.dksa.service.BookingService;

@RestController
@RequestMapping("/api/admin/bookings")
public class AdminBookingController {

    private final BookingService
            bookingService;

    public AdminBookingController(
            BookingService bookingService) {

        this.bookingService =
                bookingService;
    }

    @GetMapping
    public ResponseEntity<
            List<AdminBookingResponse>>
    getAllBookings() {

        return ResponseEntity.ok(
                bookingService
                        .getAllBookings());
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
}