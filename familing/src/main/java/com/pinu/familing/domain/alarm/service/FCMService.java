package com.pinu.familing.domain.alarm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.pinu.familing.domain.alarm.entity.Alarm;
import com.pinu.familing.domain.user.entity.User;
import com.pinu.familing.domain.user.repository.UserRepository;
import com.pinu.familing.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import static com.pinu.familing.global.error.ExceptionCode.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    public void sendFCMAlarm(String token,Alarm alarm) {

        Message message = Message.builder()
                .setNotification(new Notification("타이틀","바디"))
                .setToken(token)
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error("FCM 발송 실패: {}", e.getMessage());
        }
    }
    public void sendFCMAlarmForTest(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Message message = Message.builder()
                .setNotification(new Notification("타이틀","바디"))
                .setToken(user.getFCMToken())
                .build();

        log.info("FCM 알람 발송: 유저정보 : {} 토큰 정보 : {} 알림 내용 : {}",user.getRealname(),user.getFCMToken(),"타이틀과 내용 ㅎ");

        try {
            firebaseMessaging.send(message);
            log.info("FCM 알람 성공: 유저정보 : {} 토큰 정보 : {} 알림 내용 : {}",user.getRealname(),user.getFCMToken(),"타이틀과 내용 ㅎ");

        } catch (FirebaseMessagingException e) {
            log.error("FCM 발송 실패: {}", e.getMessage());
        }
    }
}
