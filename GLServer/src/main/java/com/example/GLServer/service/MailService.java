package com.example.GLServer.service;

import com.example.GLServer.repository.ResponseData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class MailService {

    private static final String AUTH_CODE_PREFIX ="AuthCode ";
    private final JavaMailSender emailSender;

    public MailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }
    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;


    public ResponseData sendEmail(String toEmail) {
        ResponseData responseData = new ResponseData();
        String authCode = this.createCode();
        SimpleMailMessage emailForm = createEmailForm(toEmail, authCode);
        try {
            emailSender.send(emailForm);
            Map<String, Object> result = new HashMap<>();
            result.put("authCode", authCode);
            System.out.println(toEmail);
            System.out.println(authCode);
            responseData.setResult(result);
        } catch (MailException e) {
            responseData.setCode(400);
            System.out.println(toEmail);
            System.out.println(authCode);
            e.printStackTrace();
        }
        return responseData;
    }

    private SimpleMailMessage createEmailForm(String toEmail, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("이메일 인증");
        String body = "";
        body += "요청하신 인증 번호입니다.\n";
        body += authCode+"\n";
        body += "감사합니다.\n";
        message.setText(body);
        return message;
    }


    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}