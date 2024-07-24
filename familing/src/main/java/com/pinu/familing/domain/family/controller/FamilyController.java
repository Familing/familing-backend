package com.pinu.familing.domain.family.controller;

import com.pinu.familing.domain.family.dto.FamilyCode;
import com.pinu.familing.domain.family.dto.FamilyName;
import com.pinu.familing.domain.family.service.FamilyService;
import com.pinu.familing.domain.user.service.UserService;
import com.pinu.familing.global.oauth.dto.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FamilyController {

    private final FamilyService familyService;
    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    /**
     * <가족 생성 로직>
     * 유저가 가족을 가지고 있는지 검사
     * 유저 정보 기반으로 코드 발급 + 가족 생성
     * 유저에 가족 정보 넣기(이거 유저 서비스로 옮길까 심히 고민 중)
     *
     */
    @PostMapping("/family")
    public ResponseEntity<?> createFamily(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody FamilyName familyName) {
        String code = familyService.registerNewFamily(customOAuth2User.getName(), familyName.name());
        familyService.addFamilyToUser(customOAuth2User.getName(),code);
        return ResponseEntity.ok("가족 생성과 추가 성공");
    }

    /**
     * <가족 등록 로직>
     * 유저가 가족을 가지고 있는지 검사 (누가 로직을 가지고 있는게 좋을까요?
     * 유효한 가족 코드 확인
     * 유저 정보 넣기
     */
    @PutMapping("/family")
    public ResponseEntity<?> registerFamily(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,@RequestBody FamilyCode familyCode) {
        familyService.addFamilyToUser(customOAuth2User.getName(),familyCode.code());
        return ResponseEntity.ok("successfully registered family");
    }



}
