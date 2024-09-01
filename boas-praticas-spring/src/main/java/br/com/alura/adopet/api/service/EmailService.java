package br.com.alura.adopet.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.set.from}")
    private String emailFrom;

    private JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void enviarEmail(String to, String subject, String text) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(emailFrom);
        email.setTo(to);
        email.setSubject(subject);
        email.setText(text);
        emailSender.send(email);
    }

}
