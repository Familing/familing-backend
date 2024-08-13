package com.pinu.familing.domain.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GptRequestMessage {
    private String model;
    private String role;
    private String message;
}