package com.example.GLServer.controller;

import com.example.GLServer.repository.ResponseData;
import com.example.GLServer.service.MainService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@ResponseBody
public class MainController {

    private final MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/main/info")
    public ResponseData mainInfoProcess(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return mainService.mainInfo(username);
    }

}
