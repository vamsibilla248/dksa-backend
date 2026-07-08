package com.dksa.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarSlotResponse {

    private LocalDate slotDate;

    private LocalTime slotTime;

    private String status;

    private String turfName;
}