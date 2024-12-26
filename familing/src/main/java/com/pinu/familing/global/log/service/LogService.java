package com.pinu.familing.global.log.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LogService {
    @Value("${log.file.path}")
    private String logFilePath;

    public List<String> getNewLogs() {
        // 파일 체크를 먼저 수행
        File logFile = new File(logFilePath);
        log.debug("로그 파일 경로: {}", logFilePath);  // 설정된 경로 확인용

        if (!logFile.exists()) {
            log.error("로그 파일을 찾을 수 없습니다. 경로: {}", logFilePath);
            return new ArrayList<>();
        }

        if (!logFile.canRead()) {
            log.error("로그 파일에 대한 읽기 권한이 없습니다. 경로: {}", logFilePath);
            return new ArrayList<>();
        }

        log.debug("파일 크기: {} bytes", logFile.length());  // 파일 크기 확인용

        try (RandomAccessFile file = new RandomAccessFile(logFile, "r")) {
            List<String> logs = new ArrayList<>();
            String line;

            while ((line = file.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                if (line.startsWith("Hibernate:")) {
                    StringBuilder sqlLog = new StringBuilder(line);
                    String nextLine;
                    while ((nextLine = file.readLine()) != null && nextLine.startsWith("    ")) {
                        sqlLog.append("\n").append(nextLine.trim());
                    }
                    logs.add(sqlLog.toString());
                    if (nextLine != null && !nextLine.trim().isEmpty()) {
                        logs.add(nextLine);
                    }
                } else {
                    logs.add(line);
                }
            }

            log.debug("읽은 로그 수: {}", logs.size());
            return logs;

        } catch (IOException e) {
            log.error("로그 파일 읽기 실패: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}