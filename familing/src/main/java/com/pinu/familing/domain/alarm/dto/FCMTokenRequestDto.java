package com.pinu.familing.domain.alarm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FCMTokenRequestDto {
    @NotBlank(message = "토큰을 입력해야 합니다.")
    String token;
}
