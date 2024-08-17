package com.pinu.familing.domain.status.dto;

import com.pinu.familing.domain.user.dto.UserResponse;
import com.pinu.familing.domain.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public record UserStatusResponse(String username,
                                 String nickname,
                                 String imageUrl,
                                 String status) {
    public UserStatusResponse(User user) {
        this(user.getUsername(), user.getNickname(), user.getImageUrl(), user.getStatus().getText());
    }
}
