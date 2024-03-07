package com.example.GLServer.controller;

import com.example.GLServer.dto.JoinDTO;
import com.example.GLServer.service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService){
        this.joinService = joinService;
    }

    @PostMapping("/join")
    public String joinController(JoinDTO joinDto){
        joinService.joinService(joinDto);
        return "ok";
    }
}
