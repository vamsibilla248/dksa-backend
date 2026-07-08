package com.dksa.service;

import com.dksa.dto.PaymentOrderRequest;
import com.dksa.dto.PaymentOrderResponse;
import com.dksa.dto.VerifyPaymentRequest;
import com.dksa.dto.VerifyPaymentResponse;

public interface PaymentService {

	PaymentOrderResponse createOrder(PaymentOrderRequest request);

	VerifyPaymentResponse verifyPayment(VerifyPaymentRequest request);
}