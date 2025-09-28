package com.pinu.familing.global.log.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LogService {
    @Value("${log.file.path}")
    private String logFilePath;

    private long lastFilePointer = 0;
    private final Object fileLock = new Object();

    public List<String> getNewLogs() {
        synchronized (fileLock) {  
            try (RandomAccessFile file = new RandomAccessFile(logFilePath, "r")) {
                long currentLength = file.length();
                
                if (lastFilePointer > currentLength) {
                    lastFilePointer = 0;
                }
                
                file.seek(lastFilePointer);

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

                lastFilePointer = file.getFilePointer();

                log.debug("읽은 로그 수: {}, 파일 포인터: {}", logs.size(), lastFilePointer);
                return logs;
            } catch (IOException e) {
                log.error("로그 파일 읽기 실패: {}", e.getMessage(), e);
                return new ArrayList<>();
            }
        }
    }
}
