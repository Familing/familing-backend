package com.pinu.familing.domain.status.controller;

import com.pinu.familing.domain.status.dto.StatusRequest;
import com.pinu.familing.domain.status.service.StatusService;
import com.pinu.familing.global.oauth.dto.CustomOAuth2User;
import com.pinu.familing.global.oauth.dto.PrincipalDetails;
import com.pinu.familing.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statuses")
public class StatusController {

    private final StatusService statusService;

    //상태조회
    @GetMapping()
    public ApiUtils.ApiResult<?> getStatusList() {
        return ApiUtils.success(statusService.getStatusList());
    }

    //유저의 상태 변경
    @PatchMapping("/users")
    public ApiUtils.ApiResult<?> changeUserStatus(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                  @RequestBody StatusRequest statusRequest) {
        statusService.changeUserStatus(principalDetails.getUsername(), statusRequest);
        return ApiUtils.success("User's status has been successfully changed.");
    }


    //유저와 가족의 상태 조회
    @GetMapping("/family")
    public ApiUtils.ApiResult<?> changeFamilyStatus(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiUtils.success(statusService.getFamilyStatusList(principalDetails.getUsername()));
    }
}