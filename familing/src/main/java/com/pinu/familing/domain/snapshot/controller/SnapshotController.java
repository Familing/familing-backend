package com.pinu.familing.domain.snapshot.controller;

import com.pinu.familing.domain.snapshot.dto.CustomPage;
import com.pinu.familing.domain.snapshot.dto.SnapshotImageRequest;
import com.pinu.familing.domain.snapshot.dto.SnapshotResponse;
import com.pinu.familing.domain.snapshot.scheduler.SnapshotScheduler;
import com.pinu.familing.domain.snapshot.service.SnapshotService;
import com.pinu.familing.global.error.CustomException;
import com.pinu.familing.global.error.ExceptionCode;
import com.pinu.familing.global.oauth.dto.CustomOAuth2User;
import com.pinu.familing.global.util.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/v1/snapshots")
@RequiredArgsConstructor
public class SnapshotController {

    private final SnapshotService snapshotService;
    private final SnapshotScheduler snapshotScheduler;

    // 특정 날짜 스냅샷 조회
    @GetMapping("/{day}")
    public ApiUtils.ApiResult<?> provideSnapshot(@PathVariable("day") LocalDate day,
                                                 @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        SnapshotResponse snapshot = snapshotService.provideSnapshot(day, customOAuth2User.getName());
        return ApiUtils.success(snapshot);
    }

    //스냅샷 페이지 조회
    @GetMapping(value = "/{day}", params = "page")
    public ApiUtils.ApiResult<?> changeSnapshot(@PathVariable("day") LocalDate day, Pageable pageable,
                                                @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Page<SnapshotResponse> snapshotPage = snapshotService.provideSnapshotPage(day, pageable, customOAuth2User.getName());
        return ApiUtils.success(new CustomPage(snapshotPage));
    }


    //스냅샷 이미지 등록
    @PostMapping("/{day}/users")
    public ApiUtils.ApiResult<?> registerSnapshotImage(@PathVariable("day") LocalDate day,
                                                       @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                       @Valid @RequestBody SnapshotImageRequest snapshotImageRequest) {
        snapshotService.createSnapshotGroup(day, customOAuth2User.getName());
        snapshotService.registerSnapshotImage(day, customOAuth2User.getName(), snapshotImageRequest);
        return ApiUtils.success("Image has been registered successfully.");
    }


    // 스냅샷 알람 수정
    @PatchMapping("/alarm")
    public ApiUtils.ApiResult<?> setSnapshotAlarm(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                  @RequestParam(name = "time") String time) {
        snapshotScheduler.scheduleSnapshotAlarm(time);
        return ApiUtils.success("Snapshot alarm has been converted successfully.");
    }


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
                    throw new CustomException(ExceptionCode.DAY_BAD_REQUEST);
                }
            }
        });
    }


}
