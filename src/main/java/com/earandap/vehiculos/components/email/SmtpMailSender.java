package com.earandap.vehiculos.components.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

/**
 * Created by angel on 30/03/16.
 */
@Component
public class SmtpMailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    public void send(String to, String subject, String body) throws MessagingException {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);

        mailMessage.setSubject(subject);
        mailMessage.setText(body);

        javaMailSender.send(mailMessage);

    }

}