package com.dksa.service;

public interface PdfService {

	byte[] generateReceiptPdf(Long bookingId);
	
	byte[] generateBookingHistoryPdf();
}