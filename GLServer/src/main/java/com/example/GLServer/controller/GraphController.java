package com.example.GLServer.controller;

import com.example.GLServer.repository.ResponseData;
import com.example.GLServer.service.GraphService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
public class GraphController {
    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping("/graph/info")
    public ResponseData graphInfoProcess(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return graphService.graphInfo(username);
    }
}
