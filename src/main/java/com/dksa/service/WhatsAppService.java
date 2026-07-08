package com.dksa.service;


public interface WhatsAppService {

    void sendBookingMessage(
            String mobile,
            Long bookingId);
}
