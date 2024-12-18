package com.pinu.familing.global.log.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class LogController {

    @GetMapping("/api/v1/log")
    public String getDeployLog() {
       Path logPath = Path.of("/home/ubuntu/deploy.log");  // 실제 로그 파일 경로
        try {
            return Files.readAllLines(logPath)
                    .stream()
                    .skip(Math.max(0, Files.readAllLines(logPath).size() - 5))
                    .toList().toString();
        } catch (IOException e) {
            throw new RuntimeException("로그 파일을 읽을 수 없습니다.", e);
        }
    }
}
