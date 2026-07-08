package com.dksa.dto;

import java.util.List;

import lombok.Data;

@Data
public class BookingRequest {

    private List<Long> slotIds;
}