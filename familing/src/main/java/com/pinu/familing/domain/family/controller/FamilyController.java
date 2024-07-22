package com.pinu.familing.domain.family.controller;

import com.pinu.familing.domain.family.dto.FamilyCode;
import com.pinu.familing.domain.family.service.FamilyService;
import com.pinu.familing.domain.user.service.UserService;
import com.pinu.familing.global.oauth.dto.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FamilyController {

    private final FamilyService familyService;
    private final UserService userService;
    public FamilyController(FamilyService familyService, UserService userService) {
        this.familyService = familyService;
        this.userService = userService;
    }

    /*
     * >  뭔가 제 생각! 가족 유무를 체크 하고 없으면 이 단계를 거치게끔 하고 싶은데요!..!!
     * >  준형님이 일단 구현을 해보라고 햇으니까 해볼게용
     *
     * 흐름
     * 근데 이 부분 프론트에서 요청을 보낼 것 같네요
     *  - 유저가 패밀리에 등록되어있는 경우
     *  - 유저가 패밀리가 없는 경우
     *
     * 이 검증도 하나의 메서드에서 할까요?
     *
     * 아니면 이건 따로 검증
     */

    /**
     * <가족 생성 로직>
     * 유저가 가족을 가지고 있는지 검사
     * 유저 정보 기반으로 코드 발급 + 가족 생성
     * 유저에 가족 정보 넣기(이거 유저 서비스로 옮길까 심히 고민 중)
     *
     */
    @PostMapping("/family")
    public ResponseEntity<?> registerFamily(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        String code = familyService.createFamily(customOAuth2User.getName());
        userService.addFamilyToUser(customOAuth2User.getName(),code);
        return ResponseEntity.ok("가족 생성과 추가 성공");
    }

    /**
     * <가족 등록 로직>
     * 유저가 가족을 가지고 있는지 검사 (누가 로직을 가지고 있는게 좋을까요?
     * 유효한 가족 코드 확인
     * 유저 정보 넣기
     */
    @PutMapping("/family")
    public ResponseEntity<?> registerFamily(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, FamilyCode familyCode) {
        String validCode = familyService.validFamilyCode(familyCode.code());
        userService.addFamilyToUser(customOAuth2User.getName(),validCode);
        return ResponseEntity.ok("successfully registered family");
    }



}
