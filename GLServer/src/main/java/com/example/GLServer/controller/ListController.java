package com.example.GLServer.controller;

import com.example.GLServer.repository.ResponseData;
import com.example.GLServer.service.ListService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
public class ListController {
    private final ListService listService;


    public ListController(ListService listService) {
        this.listService = listService;
    }

    @GetMapping("list/info")
    public ResponseData listInfoProcess(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return listService.listInfo(username);
    }

}
