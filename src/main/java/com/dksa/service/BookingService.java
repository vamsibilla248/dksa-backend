package com.dksa.service;

import java.util.List;

import com.dksa.dto.AdminBookingResponse;
import com.dksa.dto.BookingRequest;
import com.dksa.dto.BookingResponse;
import com.dksa.dto.MonthlyReportResponse;
import com.dksa.dto.OfflineBookingRequest;
import com.dksa.dto.ReceiptResponse;
import com.dksa.dto.ReportResponse;
import com.dksa.entity.Booking;

public interface BookingService {

	BookingResponse createBooking(BookingRequest request);

	List<BookingResponse> getMyBookings();

	ReceiptResponse getReceipt(Long bookingId);

	BookingResponse createDirectBooking(BookingRequest request);

	ReportResponse getReports();

	List<AdminBookingResponse> getAllBookings();

	void cancelBooking(Long bookingId);

	List<MonthlyReportResponse> getMonthlyReports();
	
	void createOfflineBooking(OfflineBookingRequest request);
	
	List<AdminBookingResponse> findAllByOrderByIdDesc();

}