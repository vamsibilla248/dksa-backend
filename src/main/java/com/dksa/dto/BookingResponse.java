package com.dksa.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.dksa.entity.PaymentStatus;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class BookingResponse {

    private Long bookingId;

    private String turfName;

    private Double totalAmount;

    private PaymentStatus paymentStatus;

    private LocalDateTime bookingTime;

    private List<Long> slotIds;

    private Integer slotCount;

    private List<String> slotTimes;
}