package com.pinu.familing.domain.alarm.controller;


import com.pinu.familing.domain.alarm.service.AlarmService;
import com.pinu.familing.global.oauth.dto.CustomOAuth2User;
import com.pinu.familing.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping("alarm")
    public ApiUtils.ApiResult<?> getAlarm(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        return ApiUtils.success(alarmService.loadAlarm(customOAuth2User.getName()));
    }

//    @PostMapping("alamr")
//    public ApiUtils.ApiResult<?> sendAlarmTest() {
//        alarmService.sendAlarm();
//    }
}
