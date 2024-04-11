package com.example.GLServer.controller;

import com.example.GLServer.repository.ResponseData;
import com.example.GLServer.service.QuestService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
public class QuestController {
    private final QuestService questService;


    public QuestController(QuestService questService) {
        this.questService = questService;
    }

    @GetMapping("quest/info")
    public ResponseData questInfoProcess(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return questService.questInfo(username);
    }
}
