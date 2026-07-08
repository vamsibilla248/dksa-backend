package com.dksa.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.dksa.entity.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReceiptResponse {

    private Long bookingId;

    private String customerName;

    private String customerEmail;

    private String turfName;

    private String orderId;

    private String paymentId;

    private Double amount;

    private PaymentStatus paymentStatus;

    private LocalDateTime bookingTime;

    private List<String> slots;
}