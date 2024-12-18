package com.pinu.familing.global.log.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/log")
public class LogController {
    private final Path logPath = Path.of("/home/ubuntu/deploy.log");
    private final int LINES_TO_READ = 30;

    private List<String> readLastNLines(int n) throws IOException {
        List<String> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(logPath.toFile()))) {
            LinkedList<String> lastNLines = new LinkedList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lastNLines.add(line);
                if (lastNLines.size() > n) {
                    lastNLines.removeFirst();
                }
            }
            return new ArrayList<>(lastNLines);
        }
    }

    @GetMapping("/all")
    public String getAllLogs() {
        try {
            return String.join("\n", readLastNLines(LINES_TO_READ));
        } catch (IOException e) {
            throw new RuntimeException("로그 파일을 읽을 수 없습니다.", e);
        }
    }

    @GetMapping("/error")
    public String getErrorLogs() {
        try {
            return readLastNLines(LINES_TO_READ).stream()
                    .filter(line -> line.contains("ERROR") || line.contains("WARN"))
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException("로그 파일을 읽을 수 없습니다.", e);
        }
    }

    @GetMapping("/sql")
    public String getSqlLogs() {
        try {
            return readLastNLines(LINES_TO_READ).stream()
                    .filter(line -> line.contains("Hibernate:") ||
                            line.trim().startsWith("select") ||
                            line.trim().startsWith("insert") ||
                            line.trim().startsWith("update") ||
                            line.trim().startsWith("delete"))
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException("로그 파일을 읽을 수 없습니다.", e);
        }
    }
}
