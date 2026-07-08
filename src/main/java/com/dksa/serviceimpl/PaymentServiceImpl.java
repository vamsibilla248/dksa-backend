package com.dksa.serviceimpl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dksa.dto.PaymentOrderRequest;
import com.dksa.dto.PaymentOrderResponse;
import com.dksa.dto.VerifyPaymentRequest;
import com.dksa.dto.VerifyPaymentResponse;
import com.dksa.entity.Booking;
import com.dksa.entity.BookingSlot;
import com.dksa.entity.Payment;
import com.dksa.entity.PaymentStatus;
import com.dksa.entity.Slot;
import com.dksa.entity.SlotStatus;
import com.dksa.entity.User;
import com.dksa.repository.BookingRepository;
import com.dksa.repository.BookingSlotRepository;
import com.dksa.repository.PaymentRepository;
import com.dksa.repository.SlotRepository;
import com.dksa.repository.UserRepository;
import com.dksa.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import jakarta.transaction.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Value("${razorpay.key.id}")
	private String keyId;

	@Value("${razorpay.key.secret}")
	private String keySecret;
	
	private final SlotRepository slotRepository;

	private final UserRepository userRepository;

	private final BookingRepository bookingRepository;

	private final BookingSlotRepository bookingSlotRepository;

	private final PaymentRepository paymentRepository;
	
	public PaymentServiceImpl(
	        SlotRepository slotRepository,
	        UserRepository userRepository,
	        BookingRepository bookingRepository,
	        BookingSlotRepository bookingSlotRepository,
	        PaymentRepository paymentRepository) {

	    this.slotRepository = slotRepository;
	    this.userRepository = userRepository;
	    this.bookingRepository = bookingRepository;
	    this.bookingSlotRepository = bookingSlotRepository;
	    this.paymentRepository = paymentRepository;
	}

	@Override
	public PaymentOrderResponse createOrder(PaymentOrderRequest request) {

		try {

			RazorpayClient client = new RazorpayClient(keyId, keySecret);

			JSONObject options = new JSONObject();

			options.put("amount", request.getAmount() * 100);

			options.put("currency", "INR");

			options.put("receipt", "receipt_" + System.currentTimeMillis());

			Order order = client.orders.create(options);

			return PaymentOrderResponse.builder().orderId(order.get("id")).amount(request.getAmount())
					.currency(order.get("currency")).build();

		} catch (Exception ex) {

			throw new RuntimeException(ex.getMessage());
		}
	}
	
	
	@Override
	@Transactional
	public VerifyPaymentResponse verifyPayment(
	        VerifyPaymentRequest request) {

	    try {

	        String payload =
	                request.getRazorpayOrderId()
	                        + "|"
	                        + request.getRazorpayPaymentId();

	        boolean valid =
	                Utils.verifySignature(
	                        payload,
	                        request.getRazorpaySignature(),
	                        keySecret);

	        if (!valid) {

	            throw new RuntimeException(
	                    "Invalid Payment Signature");
	        }

	        Authentication authentication =
	                SecurityContextHolder
	                        .getContext()
	                        .getAuthentication();

	        String email =
	                authentication.getName();

	        User user =
	                userRepository.findByEmail(email)
	                        .orElseThrow(() ->
	                                new RuntimeException(
	                                        "User not found"));

	        List<Slot> slots =
	                slotRepository.findAllById(
	                        request.getSlotIds());

	        if (slots.size() < 2) {

	            throw new RuntimeException(
	                    "Minimum 2 slots required");
	        }

	        Long turfId =
	                slots.get(0)
	                        .getTurf()
	                        .getId();

	        boolean sameTurf =
	                slots.stream()
	                        .allMatch(slot ->
	                                slot.getTurf()
	                                        .getId()
	                                        .equals(turfId));

	        if (!sameTurf) {

	            throw new RuntimeException(
	                    "Slots must belong to same turf");
	        }

	        boolean allOpen =
	                slots.stream()
	                        .allMatch(slot ->
	                                slot.getStatus()
	                                        == SlotStatus.OPEN);

	        if (!allOpen) {

	            throw new RuntimeException(
	                    "Slots already booked");
	        }

	        double totalAmount =
	                slots.stream()
	                        .mapToDouble(
	                                Slot::getPrice)
	                        .sum();

	        Booking booking =
	                Booking.builder()
	                        .user(user)
	                        .turf(
	                                slots.get(0)
	                                        .getTurf())
	                        .totalAmount(
	                                totalAmount)
	                        .paymentStatus(
	                                PaymentStatus.PAID)
	                        .build();

	        booking =
	                bookingRepository.save(
	                        booking);

	        for (Slot slot : slots) {

	            BookingSlot bookingSlot =
	                    BookingSlot.builder()
	                            .booking(booking)
	                            .slot(slot)
	                            .build();

	            bookingSlotRepository.save(
	                    bookingSlot);

	            slot.setStatus(
	                    SlotStatus.BOOKED);

	            slotRepository.save(slot);
	        }

	        Payment payment =
	                Payment.builder()
	                        .booking(booking)
	                        .razorpayOrderId(
	                                request.getRazorpayOrderId())
	                        .razorpayPaymentId(
	                                request.getRazorpayPaymentId())
	                        .razorpaySignature(
	                                request.getRazorpaySignature())
	                        .amount(totalAmount)
	                        .paymentStatus(
	                                PaymentStatus.PAID)
	                        .build();

	        paymentRepository.save(
	                payment);

	        return VerifyPaymentResponse
	                .builder()
	                .success(true)
	                .message(
	                        "Payment Verified And Booking Created")
	                .build();

	    } catch (Exception ex) {

	        throw new RuntimeException(
	                ex.getMessage());
	    }
	}
}