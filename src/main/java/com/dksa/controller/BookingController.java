package com.dksa.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dksa.dto.BookingRequest;
import com.dksa.dto.BookingResponse;
import com.dksa.dto.ReceiptResponse;
import com.dksa.dto.ReportResponse;
import com.dksa.service.BookingService;
import com.dksa.service.PdfService;

@RestController
@RequestMapping("/api/customer/bookings")
public class BookingController {

	private final BookingService bookingService;
	private final PdfService pdfService;

	public BookingController(
	        BookingService bookingService,
	        PdfService pdfService) {

	    this.bookingService = bookingService;
	    this.pdfService = pdfService;
	}

	@PostMapping
	public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request) {

		return ResponseEntity.ok(bookingService.createBooking(request));
	}

	@GetMapping("/my")
	public ResponseEntity<List<BookingResponse>> getMyBookings() {

		return ResponseEntity.ok(bookingService.getMyBookings());
	}
	
	@GetMapping("/receipt/{bookingId}")
	public ResponseEntity<ReceiptResponse> getReceipt(@PathVariable Long bookingId) {

		return ResponseEntity.ok(bookingService.getReceipt(bookingId));
	}
	
	@GetMapping("/receipt/pdf/{bookingId}")
	public ResponseEntity<byte[]> downloadReceiptPdf(@PathVariable Long bookingId) {
		
		System.out.println("---downloadReceiptPdf-------"+bookingId);

		byte[] pdf = pdfService.generateReceiptPdf(bookingId);

		return ResponseEntity.ok()

				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=receipt.pdf")

				.contentType(MediaType.APPLICATION_PDF)

				.body(pdf);
	}
	
	@PostMapping("/direct")
	public ResponseEntity<BookingResponse> createDirectBooking(@RequestBody BookingRequest request) {

		return ResponseEntity.ok(bookingService.createDirectBooking(request));
	}
	
	@GetMapping("/reports")
	public ResponseEntity<ReportResponse>
	getReports() {

	    return ResponseEntity.ok(
	            bookingService.getReports());
	}
	
	
	@GetMapping("/history/pdf")
	public ResponseEntity<byte[]>
	downloadBookingHistory() {

	    byte[] pdf =
	            pdfService
	                    .generateBookingHistoryPdf();

	    return ResponseEntity.ok()

	            .header(
	                HttpHeaders
	                    .CONTENT_DISPOSITION,
	                "attachment; filename=booking-history.pdf")

	            .contentType(
	                MediaType.APPLICATION_PDF)

	            .body(pdf);
	}
}