package com.pinu.familing.domain.alarm.service;

import static org.junit.jupiter.api.Assertions.*;



import com.pinu.familing.IntegrationTestSupport;
import com.pinu.familing.domain.alarm.AlarmType;
import com.pinu.familing.domain.alarm.entity.Alarm;
import com.pinu.familing.domain.alarm.repository.AlarmRepository;
import com.pinu.familing.domain.user.entity.User;
import com.pinu.familing.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class AlarmServiceTest extends IntegrationTestSupport {

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private UserRepository userRepository;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        sender = User.builder()
                .nickname("Sender")
                .username("senderUser")
                .profileImg("senderProfileImg.png")
                .build();
        receiver = User.builder()
                .nickname("Receiver")
                .username("receiverUser")
                .build();
        userRepository.save(sender);
        userRepository.save(receiver);
    }

    @AfterEach
    void tearDown() {
        alarmRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("LOVECARD_RECEIVE 알림을 성공적으로 보낸다.")
    void testSendLoveCardReceiveAlarm() {
        // when
        alarmService.sendAlarm(sender, receiver, AlarmType.LOVECARD_RECEIVE);

        // then
        Alarm alarm = alarmRepository.findAll().get(0);
        assertThat(alarm.getSender()).isEqualTo(sender);
        assertThat(alarm.getReceiver()).isEqualTo(receiver);
        assertThat(alarm.getAlarmType()).isEqualTo(AlarmType.LOVECARD_RECEIVE);
        assertThat(alarm.getMessage()).isEqualTo(sender.getNickname() + "님이 애정카드를 보냈어요.");
        assertThat(alarm.getAlarmImg()).isEqualTo(sender.getProfileImg());
        assertThat(alarm.getIsRead()).isFalse();
    }

    @Test
    @DisplayName("SNAPSHOT_REGISTER 알림을 성공적으로 보낸다.")
    void testSendSnapshotRegisterAlarm() {
        // when
        alarmService.sendAlarm(sender, receiver, AlarmType.SNAPSHOT_REGISTER);

        // then
        Alarm alarm = alarmRepository.findAll().get(0);
        assertThat(alarm.getSender()).isEqualTo(sender);
        assertThat(alarm.getReceiver()).isEqualTo(receiver);
        assertThat(alarm.getAlarmType()).isEqualTo(AlarmType.SNAPSHOT_REGISTER);
        assertThat(alarm.getMessage()).isEqualTo(sender.getNickname() + "님이 SnapShot에 사진을 등록했어요.");
        assertThat(alarm.getAlarmImg()).isEqualTo(sender.getProfileImg());
        assertThat(alarm.getIsRead()).isFalse();
    }

    @Test
    @DisplayName("SNAPSHOT_SUBJECT 알림을 성공적으로 보낸다.")
    void testSendSnapshotSubjectAlarm() {
        // when
        alarmService.sendAlarm(sender, receiver, AlarmType.SNAPSHOT_SUBJECT);

        // then
        Alarm alarm = alarmRepository.findAll().get(0);
        assertThat(alarm.getSender()).isEqualTo(sender);
        assertThat(alarm.getReceiver()).isEqualTo(receiver);
        assertThat(alarm.getAlarmType()).isEqualTo(AlarmType.SNAPSHOT_SUBJECT);
        assertThat(alarm.getMessage()).isEqualTo("Snap Shot의 주제 등록되었어요.");
        assertThat(alarm.getAlarmImg()).isEmpty();
        assertThat(alarm.getIsRead()).isFalse();
    }
}