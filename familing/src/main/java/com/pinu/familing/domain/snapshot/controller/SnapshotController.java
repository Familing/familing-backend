package com.pinu.familing.domain.snapshot.controller;

import com.pinu.familing.domain.snapshot.dto.SnapshotImageRequest;
import com.pinu.familing.domain.snapshot.dto.SnapshotResponse;
import com.pinu.familing.domain.snapshot.service.SnapshotService;
import com.pinu.familing.domain.snapshot.service.TitleService;
import com.pinu.familing.global.oauth.dto.CustomOAuth2User;
import com.pinu.familing.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.beans.PropertyEditorSupport;

@RestController
@RequestMapping("/api/v1/snapshots")
@RequiredArgsConstructor
public class SnapshotController {

    private final SnapshotService snapshotService;
    private final TitleService titleService;

//    // 특정 날짜 주제 조회
//    @GetMapping("/title/{day}")
//    public ApiUtils.ApiResult<?> changeSnapshot(@PathVariable("day") LocalDateTime localDateTime) {
//        TitleResponse titleResponse = snapshotService.provideTodayTitle(localDateTime);
//        return ApiUtils.success(titleResponse);
//    }
//
    // 특정 날짜 스냅샷 조회
    @GetMapping("/{day}")
    public ApiUtils.ApiResult<?> changeSnapshot(@PathVariable("day") LocalDate day,
                                                @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        SnapshotResponse snapshot = snapshotService.offerSnapshot(day, customOAuth2User.getName());
        return ApiUtils.success(snapshot);
    }
//
//    //스냅샷 페이지네이션 조회
//    @GetMapping
//    public ApiUtils.ApiResult<?> changeSnapshot(@RequestParam(name = "day") LocalDateTime day, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
//        snapshotService.provideTodayTitle();
//    }
//
//
    //스냅샷 이미지 등록
    @PostMapping("/{day}/photos")
    public ApiUtils.ApiResult<?> registerSnapshotImage(@PathVariable("day") LocalDate day,
                                                  @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                  @RequestBody SnapshotImageRequest snapshotImageRequest) {
        snapshotService.registerSnapshotImage(day, customOAuth2User.getName(), snapshotImageRequest);
        return ApiUtils.success("Snapshot has been registered.");
    }

    //스냅샷 생성 등록
    @PostMapping("/{day}")
    public ApiUtils.ApiResult<?> createSnapshotGroup(@PathVariable("day") LocalDate day,
                                                  @AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        snapshotService.createSnapshotGroup(day, customOAuth2User.getName());
        return ApiUtils.success("Snapshot has been registered.");
    }
//
//    // 스냅샷 수정
//    @PatchMapping("/photo")
//    public ApiUtils.ApiResult<?> changeSnapshot(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
//                                                @RequestBody SnapshotImageRequest snapshotImageRequest) {
//        snapshotService.changeSnapshot(customOAuth2User.getName(), snapshotImageRequest);
//        return ApiUtils.success("Snapshot has been registered.");
//    }
//



    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //LocalDate 타입의 필드에대해서만 변환
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {

            @Override
            public void setAsText(String text) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                    LocalDate date = LocalDate.parse(text, formatter);
                    setValue(date);
                } catch (DateTimeParseException e) {
                    // Handle invalid date format
                    throw new IllegalArgumentException("Invalid date format. Please use yyyyMMdd.", e);
                }
            }
        });
    }


}
