package com.pinu.familing.domain.snapshot.scheduler;

import com.pinu.familing.domain.family.entity.Family;
import com.pinu.familing.domain.family.repository.FamilyRepository;
import com.pinu.familing.domain.snapshot.service.SnapshotAlarmService;
import com.pinu.familing.domain.snapshot.service.SnapshotService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Component
@RequiredArgsConstructor
public class SnapshotScheduler {

    private final TaskScheduler taskScheduler = new ConcurrentTaskScheduler(); // 스레드 풀 설정
    private ScheduledFuture<?> scheduledFuture; // `final` 제거
    private final FamilyRepository familyRepository;
    private final SnapshotAlarmService snapshotAlarmService;
    private final SnapshotService snapshotService;

    /*
        0: 초를 의미하며, 매 시간의 0초에 작업을 시작합니다.
        0: 분을 의미하며, 매 시간의 0분에 작업을 시작합니다.
        *: 시간 필드에 대한 설정으로, 모든 시간(즉, 하루의 모든 정각)에 실행하도록 설정합니다.
        *: 일자 필드로, 매일 실행됩니다.
        *: 월 필드로, 매달 실행됩니다.
        ?: 요일 필드로, 요일은 특정하지 않습니다.

        @Scheduled(cron = "0 0 0 * * ?") //매일 정각
        @Scheduled(cron = "0 0 1 * * ?") //매일 새벽 한시
     */


    // 매 분마다 실행
    @Scheduled(cron = "0 55 1 * * ?")
    public void createSnapshotAlarmChangeInBatches() {
        snapshotAlarmService.changeAllAlarmChangeRequest();
    }

    // 매 분마다 실행
    @Scheduled(cron = "0 * * * * ?")
    public void sendSnapshotAlarm() {
        // 현재 시간을 분 단위로 자름
        LocalTime currentTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDate currentDate = LocalDate.now();
        // 모든 가족을 가져옴
        List<Family> families = familyRepository.findAllBySnapshotAlarmTime(currentTime);

        families.forEach((family) -> snapshotService.createFamilySnapshotEntity(family, currentDate));

        for (Family family : families) {
            System.out.println(family.getFamilyName() + ": 알람! ");
        }
    }


    //동적인 스케줄러 (이후)
    public void scheduleSnapshotAlarm(String timeString) {
        // 입력된 시간을 파싱
        LocalTime targetTime = LocalTime.parse(timeString);
        LocalDateTime targetDateTime = LocalDateTime.of(LocalDate.now(), targetTime);

        // 현재 시간과 비교하여 이미 지났으면 다음 날로 설정
        if (targetDateTime.isBefore(LocalDateTime.now())) {
            targetDateTime = targetDateTime.plusDays(1);
        }

        // 입력 시간을 UTC로 변환
        Instant targetInstant = targetDateTime.atZone(ZoneId.systemDefault()).toInstant();

        // 기존 작업 취소
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }

        // 새로운 작업 등록
        scheduledFuture = taskScheduler.schedule(
                () -> {
                    System.out.println("알림: 설정된 시간입니다!");
                    scheduleSnapshotAlarm(timeString); // 다음 알람도 등록
                },
                targetInstant
        );
    }
}
