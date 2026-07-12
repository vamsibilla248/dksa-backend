package com.dksa.dto;

import java.time.LocalDateTime;

import com.dksa.entity.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminBookingResponse {

    private Long bookingId;

    private String customerName;

    private String customerEmail;

    private String turfName;

    private Double totalAmount;

    private PaymentStatus paymentStatus;

    private LocalDateTime bookingTime;
    
}