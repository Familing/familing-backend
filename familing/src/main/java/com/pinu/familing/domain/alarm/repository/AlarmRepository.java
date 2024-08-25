package com.pinu.familing.domain.alarm.repository;

import com.pinu.familing.domain.alarm.entity.Alarm;
import com.pinu.familing.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    // 특정 수신자와 읽지 않은 상태의 알람 목록을 조회하는 메서드
    List<Alarm> findByReceiverAndIsReadFalse(User receiver);

}
