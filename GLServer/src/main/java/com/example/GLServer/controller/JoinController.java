package com.example.GLServer.controller;

import com.example.GLServer.dto.JoinInfoDTO;
import com.example.GLServer.dto.UsernamePasswordDTO;
import com.example.GLServer.repository.ResponseData;
import com.example.GLServer.service.JoinService;
import com.example.GLServer.service.MailService;
import com.example.GLServer.service.PhoneService;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@ResponseBody
public class JoinController {

    private final JoinService joinService;

    private final MailService mailService;
    private final PhoneService phoneService;


    public JoinController(JoinService joinService, MailService mailService, PhoneService phoneService){
        this.joinService = joinService;
        this.mailService = mailService;
        this.phoneService = phoneService;
    }

    @PostMapping("/signup/auth")
    public ResponseData joinAuthProcess(@RequestParam("emailPhone") String emailPhone, @RequestParam("type") Boolean type) throws UnsupportedEncodingException {
//        joinService.joinAuth(emailPhone, type);
        if(type){
            return mailService.sendEmail(emailPhone);
        }else{
            return phoneService.sendPhone(emailPhone);
        }
    }

    @PostMapping("/signup/auth/check")
    public ResponseData joinAuthCheckProcess(@RequestParam("emailPhone") String emailPhone, @RequestParam("authCode") String authCode, @RequestParam("type") Boolean type){
        //        joinService.joinAuthCheck(emailPhone, authCode);
        if(type){
            return mailService.authCheckEmail(emailPhone, authCode);
        }else{
            return phoneService.authCheckPhone(emailPhone, authCode);
        }
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
