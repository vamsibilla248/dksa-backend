package com.dksa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dksa.dto.PaymentOrderRequest;
import com.dksa.dto.PaymentOrderResponse;
import com.dksa.dto.VerifyPaymentRequest;
import com.dksa.dto.VerifyPaymentResponse;
import com.dksa.service.PaymentService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {

		this.paymentService = paymentService;
	}

	@PostMapping("/create-order")
	public ResponseEntity<PaymentOrderResponse> createOrder(@RequestBody PaymentOrderRequest request) {

		return ResponseEntity.ok(paymentService.createOrder(request));
	}

	@PostMapping("/verify")
	public ResponseEntity<VerifyPaymentResponse> verifyPayment(@RequestBody VerifyPaymentRequest request) {

		return ResponseEntity.ok(paymentService.verifyPayment(request));
	}
}