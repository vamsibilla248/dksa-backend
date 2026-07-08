package com.dksa.service;

public interface EmailService {

    void sendBookingConfirmation(
            String email,
            String customerName,
            Long bookingId);
    
    void sendReceiptEmail(
            String email,
            String customerName,
            byte[] pdf);
}