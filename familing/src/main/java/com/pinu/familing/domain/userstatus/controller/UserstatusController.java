package com.pinu.familing.domain.userstatus.controller;

import com.pinu.familing.domain.userstatus.dto.UserstatusRequest;
import com.pinu.familing.domain.userstatus.entity.Userstatus;
import com.pinu.familing.domain.userstatus.service.UserstatusService;
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
public class UserstatusController {

    private final UserstatusService userstatusService;

    //상태조회
    @GetMapping()
    public ApiUtils.ApiResult<?> getUserstatusList() {
        return ApiUtils.success(userstatusService.getUserstatusList());
    }

    //유저의 상태 변경
    @PatchMapping("/users")
    public ApiUtils.ApiResult<?> changeUserstatus(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                  UserstatusRequest userstatusRequest) {
        userstatusService.changeUserstatus(customOAuth2User.getName(), userstatusRequest);
        return ApiUtils.success("User's status has been successfully changed.");
    }


    //가족과 유저의 상태 조회

}
