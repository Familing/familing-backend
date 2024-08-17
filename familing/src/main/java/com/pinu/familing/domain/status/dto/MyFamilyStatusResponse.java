package com.pinu.familing.domain.status.dto;

import com.pinu.familing.domain.family.entity.Family;
import com.pinu.familing.domain.user.dto.UserResponse;
import com.pinu.familing.domain.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public record MyFamilyStatusResponse(List<UserStatusResponse> family) {

    public MyFamilyStatusResponse(User user) {
        this(user.getFamily().getUsers().stream()
                        .filter(familyUser -> !familyUser.equals(user)) // user와 동일한 객체는 건너뜀
                        .map(UserStatusResponse::new) // UserResponse로 변환
                        .collect(Collectors.toList()) // List로 변환
        );
    }

}
