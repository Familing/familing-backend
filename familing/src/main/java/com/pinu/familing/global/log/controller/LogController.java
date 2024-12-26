package com.pinu.familing.global.log.controller;

import com.pinu.familing.global.log.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/log")
public class LogController {
    private final LogService logService;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private static final Long TIMEOUT = 60L * 1000 * 60; // 60 minutes

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamLogs() {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        emitters.add(emitter);

        // 클라이언트 연결이 끊어졌을 때 처리
        emitter.onCompletion(() -> {
            log.debug("SSE 연결 완료");
            emitters.remove(emitter);
        });

        emitter.onTimeout(() -> {
            log.debug("SSE 연결 타임아웃");
            emitter.complete();
            emitters.remove(emitter);
        });

        // 에러 발생 시 처리
        emitter.onError(ex -> {
            log.error("SSE 에러 발생", ex);
            emitter.complete();
            emitters.remove(emitter);
        });

        try {
            // 초기 데이터 전송
            List<String> initialLogs = logService.getNewLogs();
            if (!initialLogs.isEmpty()) {
                emitter.send(SseEmitter.event()
                        .name("logs")
                        .data(initialLogs, MediaType.APPLICATION_JSON));
            }
        } catch (IOException e) {
            log.error("초기 로그 전송 실패", e);
            emitters.remove(emitter);
            emitter.completeWithError(e);
        }

        return emitter;
    }

    // 주기적으로 새로운 로그 확인 및 전송
    //@Scheduled(fixedDelay = 1000)
    public void checkNewLogs() {
        if (emitters.isEmpty()) {
            return;
        }

        List<String> newLogs = logService.getNewLogs();
        if (!newLogs.isEmpty()) {
            List<SseEmitter> deadEmitters = new ArrayList<>();

            emitters.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event()
                            .name("logs")
                            .data(newLogs, MediaType.APPLICATION_JSON));
                } catch (IOException e) {
                    deadEmitters.add(emitter);
                    log.error("로그 전송 실패", e);
                }
            });

            emitters.removeAll(deadEmitters);
        }
    }
}