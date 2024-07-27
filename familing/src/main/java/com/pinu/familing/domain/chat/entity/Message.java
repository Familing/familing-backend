package com.pinu.familing.domain.chat.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
//카프카로 전송되기 위한 직렬화된 메시지
public class Message implements Serializable {

    private String id;

    @NotNull
    private Long chatRoomId;

    @NotNull
    private Long chatId;

    @NotNull
    private String contentType;

    @NotNull
    private String content;

    private String senderUsername;

    private String senderNickname;

    private Long senderId;

    private long sendTime;

    public void setId(String id) {
        this.id = id;
    }

    public void setSendTimeAndSender(LocalDateTime sendTime, Long senderId, String senderUsername, String senderNickname) {
        this.senderUsername = senderUsername;
        this.sendTime = sendTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.senderId = senderId;
        this.senderNickname = senderNickname;
    }

    public Chatting convertEntity() {
        return Chatting.builder()
                .senderUsername(senderUsername)
                .senderId(senderId)
                .chatRoomId(chatRoomId)
                .contentType(contentType)
                .content(content)
                .sendDate(Instant.ofEpochMilli(sendTime).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime())
                .build();
    }

}