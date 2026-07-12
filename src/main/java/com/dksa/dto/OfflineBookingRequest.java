package com.dksa.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class OfflineBookingRequest {

    private Long customerId;

    private Long turfId;

    private LocalDate bookingDate;

    private List<Long> slotIds;

}
