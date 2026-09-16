package com.example.prabhim.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otp, String purpose) {
        String subject = "verification".equalsIgnoreCase(purpose) || "registration".equalsIgnoreCase(purpose)
                ? "Your Registration OTP"
                : "Your Password Reset OTP";
        String messageBody = String.format("Hello,\n\nYour OTP for %s is: %s\nThis OTP is valid for 10 minutes.\n\nRegards,\nPrabhim Team", purpose, otp);

        log.info("Sending OTP [{}] to email [{}] for purpose [{}]", otp, toEmail, purpose);

        if (mailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(toEmail);
                message.setSubject(subject);
                message.setText(messageBody);
                mailSender.send(message);
                log.info("OTP email successfully dispatched to {}", toEmail);
            } catch (Exception e) {
                log.warn("Failed to deliver email to {}: {}. (OTP logged to console: {})", toEmail, e.getMessage(), otp);
            }
        }
    }
}
