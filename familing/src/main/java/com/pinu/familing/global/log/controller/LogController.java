package com.pinu.familing.global.log.controller;

import com.pinu.familing.global.log.dto.LogResponse;
import com.pinu.familing.global.log.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/log")
@Slf4j
public class LogController {
    private final LogService logService;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private static final Long TIMEOUT = Long.MAX_VALUE;

//    // 기본 로그 조회 API - 향후 필터링 기능 추가를 위해 유지
//    @GetMapping
//    public LogResponse getLogs(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "100") int size
//    ) {
//        return logService.getLogsByPage(page, size);
//    }

    @GetMapping("/stream")
    public SseEmitter streamLogs() {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        // Send initial data
        try {
            List<String> initialLogs = logService.getNewLogs();
            emitter.send(SseEmitter.event().data(initialLogs));
        } catch (IOException e) {
            log.error("Failed to send initial logs", e);
            emitter.complete();
        }

        return emitter;
    }

    @Scheduled(fixedDelay = 1000)
    public void checkNewLogs() {
        if (emitters.isEmpty()) return;

        List<String> newLogs = logService.getNewLogs();
        if (!newLogs.isEmpty()) {
            emitters.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().data(newLogs));
                } catch (IOException e) {
                    emitters.remove(emitter);
                }
            });
        }
    }
}