package com.pinu.familing.domain.alarm.service;

import com.pinu.familing.domain.alarm.AlarmType;
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

//    @Transactional(readOnly = true)
//    public void loadAlarm(String username){
//        User user = userRepository.findByUsername(username).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
//        alarmRepository.findAllByUn
//    }
}
