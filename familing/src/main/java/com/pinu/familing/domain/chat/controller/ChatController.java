package com.pinu.familing.domain.chat.controller;

import com.pinu.familing.domain.chat.dto.ChatRoomInfoDto;
import com.pinu.familing.domain.chat.service.ChatService;
import com.pinu.familing.domain.chat.dto.ChattingHistoryResponseDto;
import com.pinu.familing.domain.chat.entity.Message;
import com.pinu.familing.global.oauth.dto.CustomOAuth2User;
import com.pinu.familing.global.util.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 자기 단톡방 채팅내역 조회
    @GetMapping("/chatroom")
    public ResponseEntity<ChattingHistoryResponseDto> chattingList(CustomOAuth2User principal) {
        ChattingHistoryResponseDto chattingList = chatService.getChattingList(principal.getName());
        return ResponseEntity.ok(chattingList);
    }

    @GetMapping("/chatroom/user")
    public ApiUtils.ApiResult<?> chatRoomInfo(CustomOAuth2User principal) {
        return ApiUtils.success(chatService.getChatRoomInfo(principal.getName()));
    }

    // ws.send("/pub/message", {}, JSON.stringify("메시지"); 로들어오는 요청을 처리한다.
    @MessageMapping("/message")
    public void sendMessage(@Valid Message message, StompHeaderAccessor accessor) {
        String username = (String) accessor.getSessionAttributes().get("username");
        chatService.sendMessage(message, username);
    }



//    // 메시지 전송 후 callback
//    @PostMapping("/chatroom/notification")
//    public ResponseEntity<Message> sendNotification(@Valid @RequestBody Message message) {
//        Message savedMessage = chatService.sendNotificationAndSaveMessage(message);
//        return ResponseEntity.ok(savedMessage);
//    }
}

