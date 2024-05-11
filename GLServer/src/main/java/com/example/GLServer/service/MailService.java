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
    private final JavaMailSender emailSender;

    private final RedisUtil redisUtil;

    public MailService(JavaMailSender emailSender, RedisUtil redisUtil) {
        this.emailSender = emailSender;
        this.redisUtil = redisUtil;
    }

    public ResponseData authCheckEmail(String email,String authCode){
        ResponseData responseData = new ResponseData();
        if(redisUtil.getData(authCode)==null){
            responseData.setCode(400);
            responseData.setMessage("인증이 만료되었습니다.");
        }
        else if(redisUtil.getData(authCode).equals(email)){
            responseData.setCode(200);
        }
        else{
            responseData.setCode(400);
            responseData.setMessage("인증에 오류가 났습니다.");
        }
        return responseData;
    }

    public ResponseData sendEmail(String emailPhone) {
        ResponseData responseData = new ResponseData();
        String authCode = this.createCode();
        SimpleMailMessage emailForm = createEmailForm(emailPhone, authCode);
        try {
            emailSender.send(emailForm);
            Map<String, Object> result = new HashMap<>();
            result.put("authCode", authCode);
            responseData.setResult(result);

            System.out.println(emailPhone);
            System.out.println(authCode);
        } catch (MailException e) {
            responseData.setCode(400);
            responseData.setMessage("이메일 전송에 오류가 났습니다.");
            e.printStackTrace();

            System.out.println(emailPhone);
            System.out.println(authCode);
        }
        redisUtil.setDataExpire(authCode, emailPhone,60*5L);
        return responseData;
    }

    private SimpleMailMessage createEmailForm(String toEmail, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("이메일 인증");
        String body = "";
        body += "[gameledger] 요청하신 인증 번호입니다.\n";
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