package com.pinu.familing.domain.status.controller;

import com.pinu.familing.domain.status.dto.StatusRequest;
import com.pinu.familing.domain.status.service.StatusService;
import com.pinu.familing.global.oauth.dto.CustomOAuth2User;
import com.pinu.familing.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statuses")
public class StatusController {

    private final StatusService StatusService;

    //상태조회
    @GetMapping()
    public ApiUtils.ApiResult<?> getStatusList() {
        return ApiUtils.success(StatusService.getStatusList());
    }

    //유저의 상태 변경
    @PatchMapping("/users")
    public ApiUtils.ApiResult<?> changeStatus(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                  StatusRequest statusRequest) {
        StatusService.changeUserStatus(customOAuth2User.getName(), statusRequest);
        return ApiUtils.success("User's status has been successfully changed.");
    }


    //가족과 유저의 상태 조회
    @GetMapping("/family")
    public ApiUtils.ApiResult<?> changeFamilystatus() {
        return ApiUtils.success("User's status has been successfully changed.");
    }
}
