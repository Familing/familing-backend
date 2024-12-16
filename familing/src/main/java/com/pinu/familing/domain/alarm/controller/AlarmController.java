package com.pinu.familing.domain.alarm.controller;


import com.pinu.familing.domain.alarm.dto.FCMTokenRequestDto;
import com.pinu.familing.domain.alarm.service.AlarmService;
import com.pinu.familing.domain.alarm.service.FCMService;
import com.pinu.familing.domain.user.service.UserService;
import com.pinu.familing.global.oauth.dto.PrincipalDetails;
import com.pinu.familing.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AlarmController {
    private final AlarmService alarmService;
    private final FCMService fcmService;
    private final UserService userService;

    //나의 알람을 불러온다.
    @GetMapping("alarms")
    public ApiUtils.ApiResult<?> getAlarm(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiUtils.success(alarmService.loadAlarm(principalDetails.getUsername()));
    }

    @PutMapping("alarms/fcm-token")
    public ApiUtils.ApiResult<?> updateFCMToken(
            @RequestBody FCMTokenRequestDto fcmToken,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        alarmService.updateFCMToken(principalDetails.getUsername(), fcmToken.getToken());
        return ApiUtils.success("Alarm token has been saved successfully.");
    }

    @PutMapping("alarms/test")
    public ApiUtils.ApiResult<?> testAlarm(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        fcmService.sendFCMAlarmForTest(principalDetails.getUsername());
        return ApiUtils.success(null);
    }

}
