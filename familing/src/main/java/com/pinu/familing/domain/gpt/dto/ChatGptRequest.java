package com.pinu.familing.domain.gpt.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatGptRequest {
    private String model;
    private List<GptRequestMessage> messages;
    private double temperature;
    private int max_tokens;
    private double top_p;
    private double frequency_penalty;
    private double presence_penalty;

    public ChatGptRequest(String model, String message) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.temperature = 1.0;
        this.max_tokens = 40;
        this.top_p = 1.0;
        this.frequency_penalty = 0.0;
        this.presence_penalty = 0.0;

        List<Object> list = List.of(new Prompt("text",message), new Prompt("text", " 위 메시지를 가족끼리 사용하는 애정 표현 하나만 만들어서 반환해줘 설명은 필요없어"));
        this.messages.add(new GptRequestMessage("user", list));

    }
}
