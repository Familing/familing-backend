package com.pinu.familing.domain.user.service;

import com.pinu.familing.domain.family.entity.Family;
import com.pinu.familing.domain.family.repositiry.FamilyRepository;
import com.pinu.familing.domain.user.dto.UserRequest;
import com.pinu.familing.domain.user.dto.UserResponse;
import com.pinu.familing.domain.user.entity.User;
import com.pinu.familing.domain.user.repository.UserRepository;
import com.pinu.familing.global.error.CustomException;
import com.pinu.familing.global.error.ExceptionCode;
import com.pinu.familing.global.oauth.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;


    public UserResponse giveUserInformation(CustomOAuth2User customOAuth2User) {
        User user = userRepository.findByUsername(customOAuth2User.getName());
        return new UserResponse(user);
    }


    @Transactional
    public void addFamilyToUser(CustomOAuth2User customOAuth2User, String code) {
        //유저 찾기
        User user = userRepository.findByUsername(customOAuth2User.getName());

        Family family = familyRepository.findByCode(code)
                .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_CODE));

        //내부에 예외 처리 부분 넣어놨습니다.
        user.registerFamily(family);
    }

    @Transactional
    public void changeUser(CustomOAuth2User customOAuth2User, UserRequest userRequest) {
        User user = userRepository.findByUsername(customOAuth2User.getName());
        user.updateValue(userRequest);
    }
}
