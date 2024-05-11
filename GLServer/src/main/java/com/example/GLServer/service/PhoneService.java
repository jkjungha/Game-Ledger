package com.example.GLServer.service;

import com.example.GLServer.repository.ResponseData;
import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;

@Service
public class PhoneService {

    @Value("${coolsms.apikey}")
    private String apiKey;

    @Value("${coolsms.apisecret}")
    private String apiSecret;

    @Value("${coolsms.fromnumber}")
    private String fromNumber;

    private  String DOMAIN = "https://api.coolsms.co.kr";

    private static final String AUTH_CODE_PREFIX ="AuthCode ";

    private final RedisUtil redisUtil;
    private DefaultMessageService messageService;

    public PhoneService(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @PostConstruct
    private void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, DOMAIN);
    }

    public ResponseData authCheckPhone(String emailPhone, String authCode) {
        ResponseData responseData = new ResponseData();
        if(redisUtil.getData(authCode)==null){
            responseData.setCode(400);
            responseData.setMessage("인증이 만료되었습니다.");
        }
        else if(redisUtil.getData(authCode).equals(emailPhone)){
            responseData.setCode(200);
        }
        else{
            responseData.setCode(400);
            responseData.setMessage("인증에 오류가 났습니다.");
        }
        return  responseData;
    }

    public void sendPhon(String toNumber) {

        Message message = new Message();
        String authCode = this.createCode();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", toNumber);
        params.put("from", fromNumber);
        params.put("type", "SMS");
        params.put("text", "[gameledger] 인증번호 "+authCode+" 를 입력하세요.");
        params.put("app_version", "test app 1.2"); // application name and version

//        try {
//            JSONObject obj = (JSONObject) message.send(params);
//            System.out.println(obj.toString());
//        } catch (CoolsmsException e) {
//            System.out.println(e.getMessage());
//            System.out.println(e.getCode());
//        }
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


    public ResponseData sendPhone(String emailPhone) {
        ResponseData responseData = new ResponseData();

        Message message = new Message();
        String authCode = this.createCode();

        message.setFrom(fromNumber);
        message.setTo(emailPhone);
        message.setText(String.format("[gameledger] 휴대전화 인증을 위한 인증 코드입니다. \n"+ authCode));

        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
        redisUtil.setDataExpire(authCode, emailPhone,60*5L);

        responseData.setResult(response);

        return responseData;
    }

}
