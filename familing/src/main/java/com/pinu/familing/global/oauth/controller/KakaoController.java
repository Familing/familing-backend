package com.pinu.familing.global.oauth.controller;

import com.pinu.familing.domain.user.entity.User;
import com.pinu.familing.domain.user.service.UserService;
import com.pinu.familing.global.error.CustomException;
import com.pinu.familing.global.error.ExceptionCode;
import com.pinu.familing.global.oauth.dto.AccessToken;
import com.pinu.familing.global.oauth.properties.KakaoProperties;
import com.pinu.familing.global.oauth.service.AuthenticationService;
import com.pinu.familing.global.oauth.service.KakaoService;
import com.pinu.familing.global.util.ApiUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;
    private final AuthenticationService authenticationService;

    @Value("${server.ip}")
    private String serverIp;

    @GetMapping("/api/v1/admin/login/oauth/kakao")
    public void requestKakaoLoginScreen(HttpServletResponse response) throws IOException {
        response.sendRedirect(kakaoService.getKakaoLoginUrl());
    }

    @GetMapping("/api/v1/admin/login/oauth/kakao/code")
    public void requestKakaoLoginScreen(@RequestParam(value = "code") String code, HttpSession session, HttpServletResponse response) throws IOException {
        String accessToken = kakaoService.getKakaoAccessToken(code).accessToken();
        User user = kakaoService.saveKakaoLoginUser(accessToken,session);
        if (!authenticationService.isAdmin(user)) {
            throw new CustomException(ExceptionCode.NO_ADMINISTRATOR_RIGHTS);
        }
        String token = authenticationService.makeToken(user);
        response.addCookie(createCookie("Authorization", token));
        response.sendRedirect("/api/v1/admin/pages/log");
    }


    @PostMapping("/api/v1/login/oauth/kakao/callback")
    public ApiUtils.ApiResult<?> saveKakaoLoginUser(@RequestBody AccessToken accessToken, HttpSession session, HttpServletResponse response) {
        User user = kakaoService.saveKakaoLoginUser(accessToken.accessToken(),session);
        String token = authenticationService.makeToken(user);
        response.addCookie(createCookie("Authorization", token));
        return ApiUtils.success("Login completed successfully");
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60);
        // https only
        //cookie.setSecure(true);
        //쿠기가 보일 위치
        cookie.setPath("/");
        // js가 쿠키를 가져가지 못하게
        cookie.setHttpOnly(true);

        cookie.setDomain(serverIp);

        return cookie;
    }
}
