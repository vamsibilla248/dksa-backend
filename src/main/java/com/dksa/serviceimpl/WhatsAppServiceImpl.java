package com.dksa.serviceimpl;

import org.springframework.stereotype.Service;

import com.dksa.service.WhatsAppService;

@Service
public class WhatsAppServiceImpl
        implements WhatsAppService {

    @Override
    public void sendBookingMessage(
            String mobile,
            Long bookingId) {

        System.out.println(
                "WhatsApp Sent To : "
                + mobile
                + " Booking : "
                + bookingId);
    }
}