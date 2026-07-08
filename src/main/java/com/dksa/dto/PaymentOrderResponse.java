package com.dksa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentOrderResponse {

    private String orderId;

    private Double amount;

    private String currency;
}