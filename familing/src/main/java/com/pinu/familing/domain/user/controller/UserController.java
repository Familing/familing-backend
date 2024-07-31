package com.pinu.familing.domain.user.controller;

import com.pinu.familing.domain.user.dto.ImageUrl;
import com.pinu.familing.domain.user.dto.Nickname;
import com.pinu.familing.domain.user.dto.Realname;
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

    //닉네임 변경
    @PostMapping("/user/nickname")
    public ApiUtils.ApiResult<?> changeNickname(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody Nickname nickname) {
        userService.changeNickname(customOAuth2User, nickname);
        return ApiUtils.success(HttpStatus.OK,"Successful nickname changed");
    }

    //진짜 이름 변경
    @PostMapping("/user/realname")
    public ApiUtils.ApiResult<?> changeRealname(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody Realname realname) {
        userService.changeRealname(customOAuth2User, realname);
        return ApiUtils.success(HttpStatus.OK,"Successful realname changed");
    }

    //프로필 변경
    @PostMapping("/user/imageurl")
    public ApiUtils.ApiResult<?> changeImageUrl(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody ImageUrl imageUrl) {
        userService.changeImageUrl(customOAuth2User, imageUrl);
        return ApiUtils.success(HttpStatus.OK,"Successful image url changed");
    }
}
