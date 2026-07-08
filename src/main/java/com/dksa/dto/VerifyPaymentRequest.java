package com.dksa.dto;

import java.util.List;

import lombok.Data;

@Data
public class VerifyPaymentRequest {

    private String razorpayOrderId;

    private String razorpayPaymentId;

    private String razorpaySignature;

    private List<Long> slotIds;
}