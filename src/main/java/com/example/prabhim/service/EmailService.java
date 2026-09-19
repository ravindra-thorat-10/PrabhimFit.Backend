package com.example.prabhim.service;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${app.mail.from-email:noreply@fit.prabhimtechnologies.in}")
    private String fromEmail;

    @Value("${app.mail.from-name:PrabhimFit}")
    private String fromName;

    @Autowired
    public EmailService(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Send OTP email for registration, email verification, or password reset.
     */
    @Async
    public void sendOtpEmail(String toEmail, String otp, String purpose) {
        boolean isRegistration = "verification".equalsIgnoreCase(purpose) || "registration".equalsIgnoreCase(purpose);
        String subject = isRegistration ? "Your PrabhimFit Registration OTP" : "Your PrabhimFit Password Reset OTP";
        String actionTitle = isRegistration ? "Account Verification" : "Password Reset";
        String actionDescription = isRegistration
                ? "Thank you for registering with <strong>PrabhimFit</strong>. Please use the following One-Time Password (OTP) to verify your account."
                : "We received a request to reset your <strong>PrabhimFit</strong> password. Use the following One-Time Password (OTP) to continue.";

        String htmlBody = buildOtpEmailTemplate(actionTitle, actionDescription, otp);
        String plainTextFallback = String.format(
                "Hello,\n\nYour OTP for %s is: %s\nThis OTP is valid for 10 minutes.\n\nRegards,\nPrabhimFit Team",
                purpose, otp
        );

        log.info("Dispatching OTP [{}] to email [{}] for purpose [{}]", otp, toEmail, purpose);
        sendHtmlEmail(toEmail, subject, htmlBody, plainTextFallback, otp);
    }

    /**
     * Send welcome email after successful account verification.
     */
    @Async
    public void sendWelcomeEmail(String toEmail, String firstName) {
        String subject = "Welcome to PrabhimFit!";
        String htmlBody = buildWelcomeEmailTemplate(firstName);
        String plainText = String.format(
                "Hello %s,\n\nWelcome to PrabhimFit! Your account is verified and ready. Start tracking your fitness and workouts today.\n\nRegards,\nPrabhimFit Team",
                firstName != null ? firstName : "Member"
        );

        sendHtmlEmail(toEmail, subject, htmlBody, plainText, null);
    }

    /**
     * Generic asynchronous HTML email sender.
     */
    @Async
    public void sendHtmlEmail(String toEmail, String subject, String htmlBody, String plainTextFallback, String debugOtp) {
        if (mailSender == null) {
            log.warn("JavaMailSender is not configured. Email to [{}] with subject [{}] was skipped. (OTP: {})", toEmail, subject, debugOtp);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(plainTextFallback, htmlBody);

            mailSender.send(message);
            log.info("Email successfully dispatched via SMTP to [{}] with subject [{}]", toEmail, subject);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send SMTP email to {}: {}. (Debug OTP: {})", toEmail, e.getMessage(), debugOtp);
        } catch (Exception e) {
            log.error("Unexpected error during SMTP email delivery to {}: {}. (Debug OTP: {})", toEmail, e.getMessage(), debugOtp);
        }
    }

    private String buildOtpEmailTemplate(String title, String description, String otp) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<style>"
                + "  body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background-color: #f1f5f9; margin: 0; padding: 0; }"
                + "  .container { max-width: 540px; margin: 40px auto; background-color: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1); border: 1px solid #e2e8f0; }"
                + "  .header { background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%); padding: 28px; text-align: center; }"
                + "  .brand { color: #38bdf8; font-size: 22px; font-weight: 800; letter-spacing: 2px; text-transform: uppercase; margin: 0; }"
                + "  .subbrand { color: #94a3b8; font-size: 12px; margin-top: 4px; text-transform: uppercase; letter-spacing: 1px; }"
                + "  .content { padding: 32px 28px; color: #334155; line-height: 1.6; font-size: 15px; }"
                + "  .title { font-size: 20px; font-weight: 700; color: #0f172a; margin-top: 0; margin-bottom: 12px; }"
                + "  .otp-card { background: #f8fafc; border: 2px dashed #94a3b8; border-radius: 10px; padding: 20px; text-align: center; margin: 24px 0; }"
                + "  .otp-code { font-family: 'Courier New', Courier, monospace; font-size: 34px; font-weight: 800; letter-spacing: 8px; color: #0284c7; margin: 0; }"
                + "  .otp-hint { font-size: 12px; color: #64748b; margin-top: 8px; }"
                + "  .note { background-color: #fef2f2; border-left: 4px solid #ef4444; padding: 12px; border-radius: 4px; font-size: 13px; color: #991b1b; margin-top: 20px; }"
                + "  .footer { background-color: #f8fafc; padding: 20px; text-align: center; font-size: 12px; color: #64748b; border-top: 1px solid #e2e8f0; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "  <div class='container'>"
                + "    <div class='header'>"
                + "      <h1 class='brand'>PRABHIM FIT</h1>"
                + "      <div class='subbrand'>Smart Fitness Platform</div>"
                + "    </div>"
                + "    <div class='content'>"
                + "      <h2 class='title'>" + title + "</h2>"
                + "      <p>" + description + "</p>"
                + "      <div class='otp-card'>"
                + "        <div class='otp-code'>" + otp + "</div>"
                + "        <div class='otp-hint'>Expires in 10 minutes</div>"
                + "      </div>"
                + "      <p>Enter this verification code on the application to complete your request.</p>"
                + "      <div class='note'>"
                + "        <strong>Security Notice:</strong> Never share this OTP with anyone. PrabhimFit staff will never ask for your verification code."
                + "      </div>"
                + "    </div>"
                + "    <div class='footer'>"
                + "      &copy; " + java.time.Year.now().getValue() + " PrabhimFit. All rights reserved.<br/>"
                + "      If you didn't request this email, you can safely ignore it."
                + "    </div>"
                + "  </div>"
                + "</body>"
                + "</html>";
    }

    private String buildWelcomeEmailTemplate(String firstName) {
        String name = (firstName != null && !firstName.isBlank()) ? firstName : "Member";
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<style>"
                + "  body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background-color: #f1f5f9; margin: 0; padding: 0; }"
                + "  .container { max-width: 540px; margin: 40px auto; background-color: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1); border: 1px solid #e2e8f0; }"
                + "  .header { background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%); padding: 28px; text-align: center; }"
                + "  .brand { color: #38bdf8; font-size: 22px; font-weight: 800; letter-spacing: 2px; text-transform: uppercase; margin: 0; }"
                + "  .subbrand { color: #94a3b8; font-size: 12px; margin-top: 4px; text-transform: uppercase; letter-spacing: 1px; }"
                + "  .content { padding: 32px 28px; color: #334155; line-height: 1.6; font-size: 15px; }"
                + "  .title { font-size: 22px; font-weight: 700; color: #0f172a; margin-top: 0; margin-bottom: 12px; }"
                + "  .button-container { text-align: center; margin: 30px 0; }"
                + "  .cta-button { display: inline-block; background-color: #0284c7; color: #ffffff !important; padding: 12px 28px; border-radius: 8px; font-weight: 600; text-decoration: none; }"
                + "  .footer { background-color: #f8fafc; padding: 20px; text-align: center; font-size: 12px; color: #64748b; border-top: 1px solid #e2e8f0; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "  <div class='container'>"
                + "    <div class='header'>"
                + "      <h1 class='brand'>PRABHIM FIT</h1>"
                + "      <div class='subbrand'>Smart Fitness Platform</div>"
                + "    </div>"
                + "    <div class='content'>"
                + "      <h2 class='title'>Welcome aboard, " + name + "! 🎉</h2>"
                + "      <p>Your email address has been verified and your account is active.</p>"
                + "      <p>With PrabhimFit, you can track daily workouts, manage personalized diet plans, view trainer schedules, and achieve your fitness goals with data-backed progress tracking.</p>"
                + "      <div class='button-container'>"
                + "        <a class='cta-button' href='https://fit.prabhimtechnologies.in'>Launch PrabhimFit</a>"
                + "      </div>"
                + "    </div>"
                + "    <div class='footer'>"
                + "      &copy; " + java.time.Year.now().getValue() + " PrabhimFit. All rights reserved.<br/>"
                + "      Support: support@prabhimtechnologies.in"
                + "    </div>"
                + "  </div>"
                + "</body>"
                + "</html>";
    }
}
