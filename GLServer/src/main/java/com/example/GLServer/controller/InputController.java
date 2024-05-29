package com.example.GLServer.controller;

import com.example.GLServer.dto.InputInfoDTO;
import com.example.GLServer.repository.ResponseData;
import com.example.GLServer.service.InputService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/settings/edit")
    public ResponseData settingsEditProcess(@RequestParam("password") String password, @RequestParam("newPassword") String newPassword){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return inputService.settingsEdit(username, password, newPassword);
    }

    @PatchMapping("/settings/logout")
    public ResponseData settingsLogoutProcess(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return inputService.settingsLogout(username);
    }

    @PatchMapping("/settings/signout")
    public ResponseData settingsSignoutProcess(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return inputService.settingsSignout(username);
    }

}
