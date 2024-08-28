package com.pinu.familing.domain.alarm.service;

import com.pinu.familing.domain.alarm.AlarmType;
import com.pinu.familing.domain.alarm.dto.AlarmDto;
import com.pinu.familing.domain.alarm.dto.AlarmResponseDto;
import com.pinu.familing.domain.alarm.entity.Alarm;
import com.pinu.familing.domain.alarm.repository.AlarmRepository;
import com.pinu.familing.domain.user.entity.User;
import com.pinu.familing.domain.user.repository.UserRepository;
import com.pinu.familing.global.error.CustomException;
import com.pinu.familing.global.error.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.pinu.familing.global.error.ExceptionCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    @Transactional
    public void sendAlarm(User sender, User receiver, AlarmType alarmType) {

        String message = "";
        String alarmImg = "";

        if (alarmType == AlarmType.LOVECARD_RECEIVE) {
            message = sender.getNickname() + "님이 애정카드를 보냈어요.";
            alarmImg = sender.getProfileImg();
        }
        else if (alarmType == AlarmType.SNAPSHOT_REGISTER) {
            message = sender.getNickname() + "님이 SnapShot에 사진을 등록했어요.";
            alarmImg = sender.getProfileImg();
        }
        else if (alarmType == AlarmType.SNAPSHOT_SUBJECT) {
            message = "Snap Shot의 주제 등록되었어요.";
            alarmImg = "";
        }

        alarmRepository.save(Alarm.builder()
                .sender(sender)
                .message(message)
                .receiver(receiver)
                .alarmType(alarmType)
                .alarmImg(alarmImg)
                .build());
    }

    @Transactional
    public AlarmResponseDto loadAlarm(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        List<Alarm> byReceiverAndIsReadFalse = alarmRepository.findByReceiverAndIsReadFalse(user);
        List<Alarm> byReceiverAndIsReadTrue = alarmRepository.findByReceiverAndIsReadTrue(user);

        AlarmResponseDto alarmResponseDto = AlarmResponseDto.builder()
                .read(byReceiverAndIsReadTrue.stream()
                        .map(AlarmDto::fromEntity)
                        .collect(Collectors.toList()))
                .unread(byReceiverAndIsReadFalse.stream()
                        .map(AlarmDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
        // 조회한 알림 읽음으로 처리
        byReceiverAndIsReadFalse.forEach(alarm -> {
            alarm.readAlarm();
            alarmRepository.save(alarm);  // 변경된 상태를 저장하기 위해 필요
        });
        return alarmResponseDto;
    }
}
