package com.pinu.familing.global.log.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager/log")
public class LogPageController {
    @GetMapping
    public String logView() {
        return "log";  // View 이름만 반환
    }
}
