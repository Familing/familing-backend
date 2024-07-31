package com.pinu.familing.domain.user.controller;

import com.pinu.familing.domain.user.dto.UserRequest;
import com.pinu.familing.domain.user.dto.UserResponse;
import com.pinu.familing.domain.user.service.UserService;
import com.pinu.familing.global.oauth.dto.CustomOAuth2User;
import com.pinu.familing.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public ApiUtils.ApiResult<UserResponse> giveUserInformation(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        UserResponse userResponse = userService.giveUserInformation(customOAuth2User);
        return ApiUtils.success(HttpStatus.OK, userResponse);
    }

    //유저 정보 업데이트
    @PostMapping("/user")
    public ApiUtils.ApiResult<?> changeNickname(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody UserRequest userRequest) {
        userService.changeUser(customOAuth2User,userRequest);
        return ApiUtils.success(HttpStatus.OK, "Successful nickname changed");
    }
}
