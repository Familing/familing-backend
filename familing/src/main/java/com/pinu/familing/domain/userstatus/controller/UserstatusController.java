package com.pinu.familing.domain.userstatus.controller;

import com.pinu.familing.domain.userstatus.service.UserstatusService;
import com.pinu.familing.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statuses")
public class UserstatusController {

    private final UserstatusService userstatusService;

    //상태조회
    @GetMapping()
    public ApiUtils.ApiResult<?> getUserStatusList() {
        return ApiUtils.success(userstatusService.getUserstatusList());
    }
}
