package com.dksa.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class SlotRequest {

    private Long turfId;

    private LocalDate bookingDate;

    private LocalTime slotTime;

    private Double price;
}