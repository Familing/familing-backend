package com.pinu.familing.domain.snapshot.controller;

import com.pinu.familing.domain.snapshot.dto.SnapshotImageRequest;
import com.pinu.familing.global.oauth.dto.CustomOAuth2User;
import com.pinu.familing.global.util.ApiUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/snapshot")
public class SnapshotController {

//    //스냅샷 등록
//    @PostMapping
//    public ApiUtils.ApiResult<?> registerSnapshot(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
//                                                  @RequestBody SnapshotImageRequest snapshotImageRequest) {
//        snapshotService.registerSnapshot(customOAuth2User.getName(), snapshotImageRequest);
//        return ApiUtils.success("Snapshot has been registered.");
//    }
//
//    // 스냅샷 수정
//    @PatchMapping
//    public ApiUtils.ApiResult<?> changeSnapshot(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
//                                                @RequestBody SnapshotImageRequest snapshotImageRequest) {
//        snapshotService.changeSnapshot(customOAuth2User.getName(), snapshotImageRequest);
//        return ApiUtils.success("Snapshot has been registered.");
//    }
//
//    // 오늘 날짜 스냅샷 주제 조회
//    @GetMapping("/title")
//    public ApiUtils.ApiResult<?> changeSnapshot(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
//        snapshotService.provideTodayTitle();
//    }
//
//    // 특정 날짜 스냅샷 주제 조회
//    @GetMapping("/title/{day}")
//    public ApiUtils.ApiResult<?> changeSnapshot(PathVariable(name = "day") ,
//                                                    @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
//        snapshotService.provideTodayTitle();
//    }
//
//    //스냅샷 페이지네이션으로 조회
//
//    //특정 날짜 스냅샷 조회


}
