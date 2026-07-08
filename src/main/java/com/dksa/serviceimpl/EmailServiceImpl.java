package com.dksa.serviceimpl;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.dksa.service.EmailService;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl
        implements EmailService {

    private final JavaMailSender mailSender;
    

    public EmailServiceImpl(
            JavaMailSender mailSender) {

        this.mailSender = mailSender;
    }

    @Override
    public void sendBookingConfirmation(
            String email,
            String customerName,
            Long bookingId) {

        SimpleMailMessage mail =
                new SimpleMailMessage();

        mail.setTo(email);

        mail.setSubject(
                "DKSA Booking Confirmed");

        mail.setText(
                "Hello "
                + customerName
                + ", your booking #"
                + bookingId
                + " has been confirmed.");

        mailSender.send(mail);
    }
    
    @Override
    public void sendReceiptEmail(
            String email,
            String customerName,
            byte[] pdf) {

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true);

            helper.setTo(email);

            helper.setSubject(
                    "DKSA Booking Receipt");

            helper.setText(
                    "Hello "
                    + customerName
                    + ",\n\nYour booking receipt is attached.");

            helper.addAttachment(
                    "Receipt.pdf",
                    new ByteArrayResource(pdf));

            mailSender.send(message);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
    
    
}