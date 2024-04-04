package com.example.GLServer.controller;

import com.example.GLServer.dto.JoinInfoDTO;
import com.example.GLServer.dto.UsernamePasswordDTO;
import com.example.GLServer.service.JoinService;
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
    public String joinAuthProcess(@RequestParam("emailPhone") String emailPhone) throws UnsupportedEncodingException {
        joinService.joinAuth(emailPhone);
        return "ok";
    }

    @PostMapping("/signup/auth/check")
    public String joinAuthCheckProcess(@RequestParam("authCode") String authCode){
        joinService.joinAuthCheck(authCode);
        return "ok";
    }

    @PostMapping("/signup/user")
    public String joinUserProcess(UsernamePasswordDTO usernamePasswordDTO){
        joinService.joinUser(usernamePasswordDTO);
        return "ok";
    }

    @PostMapping("/signup/input")
    public String joinInputProcess(JoinInfoDTO joinInfoDto){
        joinService.joinInput(joinInfoDto);
        return "ok";
    }

}
