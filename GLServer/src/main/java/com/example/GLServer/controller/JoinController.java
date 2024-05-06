package com.example.GLServer.controller;

import com.example.GLServer.dto.JoinInfoDTO;
import com.example.GLServer.dto.UsernamePasswordDTO;
import com.example.GLServer.repository.ResponseData;
import com.example.GLServer.service.JoinService;
import com.example.GLServer.service.MailService;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@RestController
@ResponseBody
public class JoinController {

    private final JoinService joinService;

    private final MailService mailService;

    public JoinController(JoinService joinService, MailService mailService){
        this.joinService = joinService;
        this.mailService = mailService;
    }

    @PostMapping("/signup/auth")
    public ResponseData joinAuthProcess(@RequestParam("emailPhone") String emailPhone, @RequestParam("type") Boolean type) throws UnsupportedEncodingException {
        if(type){
            return mailService.sendEmail(emailPhone);
        }else{
            ResponseData responseData = new ResponseData();
            return responseData;
        }
    }

    @PostMapping("/signup/auth/check")
    public ResponseData joinAuthCheckProcess(@RequestParam("authCode") String authCode){
        return joinService.joinAuthCheck(authCode);
    }

    @PostMapping("/signup/user")
    public ResponseData joinUserProcess(UsernamePasswordDTO usernamePasswordDTO){
        return joinService.joinUser(usernamePasswordDTO);
    }

    @PostMapping("/signup/input")
    public ResponseData joinInputProcess(JoinInfoDTO joinInfoDto){
        return joinService.joinInput(joinInfoDto);
    }

}
