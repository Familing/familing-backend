package com.pinu.familing.domain.alarm.dto;

import com.pinu.familing.domain.alarm.AlarmType;
import com.pinu.familing.domain.alarm.entity.Alarm;

public record AlarmDto(
        Long id,
        Boolean isRead,
        String message,
        AlarmType alarmType,
        String alarmImg
) {
    public static AlarmDto fromEntity(Alarm alarm) {
        return new AlarmDto(
                alarm.getId(),
                alarm.getIsRead(),
                alarm.getMessage(),
                alarm.getAlarmType(),
                alarm.getAlarmImg()
        );
    }
}
