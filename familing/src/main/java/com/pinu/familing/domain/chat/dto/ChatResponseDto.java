package com.pinu.familing.domain.chat.dto;
import com.pinu.familing.domain.chat.entity.Chatting;

import java.time.ZoneId;

public record ChatResponseDto(
        String id,
        Long chatRoomNo,
        Long senderNo,
        String senderName,
        String contentType,
        String content,
        long sendDate,
        boolean isMine
) {
    public ChatResponseDto(Chatting chatting, Long userNo) {
        this(
                chatting.getId(),
                chatting.getChatRoomNo(),
                chatting.getSenderNo(),
                chatting.getSenderName(),
                chatting.getContentType(),
                chatting.getContent(),
                chatting.getSendDate().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli(),
                chatting.getSenderNo().equals(userNo)
        );
    }

}