package com.example.userservice.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service("EmailService")
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends a simple email with the specified recipient, subject, and text.
     * @param to the recipient's email address
     * @param subject the subject of the email
     * @param text the content/body of the email
     */
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("your-email@gmail.com"); // Optional: specify the "from" email address

        mailSender.send(message);
    }

    /**
     * Sends an HTML email with the specified recipient, subject, and HTML content.
     * @param to the recipient's email address
     * @param subject the subject of the email
     * @param htmlBody the HTML content/body of the email
     * @throws MessagingException if an error occurs while creating or sending the email
     */
    public void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true); // Set to true to enable HTML

        mailSender.send(mimeMessage);
    }
}
