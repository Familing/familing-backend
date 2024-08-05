package com.pinu.familing.domain.user.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pinu.familing.domain.user.entity.User;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserResponse(String username,
                           String nickname,
                           String realname,
                           String imageUrl) {

    public UserResponse(User user) {
        this(
                user.getUsername(),
                user.getNickname(),
                user.getRealname(),
                user.getImageUrl()
        );
    }
}
