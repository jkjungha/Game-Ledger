package com.example.GLServer.controller;

import com.example.GLServer.dto.InputInfoDTO;
import com.example.GLServer.repository.ResponseData;
import com.example.GLServer.service.InputService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
public class InputController {
    private InputService inputService;

    public InputController(InputService inputService) {
        this.inputService = inputService;
    }

    @PostMapping("/input/info")
    public ResponseData inputInfoProcess(InputInfoDTO inputInfoDTO){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return inputService.inputInfo(username, inputInfoDTO);
    }
}
