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

    public JoinController(JoinService joinService){
        this.joinService = joinService;
    }

    @PostMapping("/signup/auth")
    public ResponseData joinAuthProcess(@RequestParam("emailPhone") String emailPhone, @RequestParam("type") Boolean type) throws UnsupportedEncodingException {
        return joinService.joinAuth(emailPhone, type);
    }

    @PostMapping("/signup/auth/check")
    public ResponseData joinAuthCheckProcess(@RequestParam("emailPhone") String emailPhone, @RequestParam("authCode") String authCode, @RequestParam("type") Boolean type){
        return joinService.joinAuthCheck(emailPhone, authCode, type);
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
