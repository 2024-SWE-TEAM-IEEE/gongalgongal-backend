package gongalgongal.gongalgongal_spring.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            javaMailSender.send(message);
            System.out.println("Email sent successfully to: " + to); // 로그 출력
        } catch (Exception e) {
            e.printStackTrace(); // 예외 출력
            throw new RuntimeException("Failed to send email. Please try again later.");
        }
    }
}