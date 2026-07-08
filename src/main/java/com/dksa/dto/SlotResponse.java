package com.dksa.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.dksa.entity.SlotStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SlotResponse {

    private Long id;

    private Long turfId;

    private String turfName;

    private LocalDate bookingDate;

    private LocalTime slotTime;

    private Double price;

    private SlotStatus status;
}