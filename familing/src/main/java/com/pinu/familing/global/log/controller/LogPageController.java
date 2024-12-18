package com.pinu.familing.global.log.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/log")
public class LogPageController {
    @GetMapping
    public String getLogPage() {
        return "log";
    }
}
