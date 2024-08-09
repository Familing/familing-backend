package com.pinu.familing.domain.snapshot.service;

import com.pinu.familing.domain.snapshot.dto.SnapshotImageRequest;
import com.pinu.familing.domain.snapshot.dto.SnapshotResponse;
import com.pinu.familing.domain.snapshot.entity.Snapshot;
import com.pinu.familing.domain.snapshot.entity.SnapshotImage;
import com.pinu.familing.domain.snapshot.repository.SnapshotRepository;
import com.pinu.familing.domain.snapshot.repository.SnapshotImageRepository;
import com.pinu.familing.domain.snapshot.scheduler.SnapshotScheduler;
import com.pinu.familing.domain.user.entity.User;
import com.pinu.familing.domain.user.repository.UserRepository;
import com.pinu.familing.global.error.CustomException;
import com.pinu.familing.global.error.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SnapshotService {

    private final SnapshotImageRepository snapshotImageRepository;
    private final SnapshotRepository snapshotRepository;
    private final UserRepository userRepository;
    private final TitleService titleService;
    private final SnapshotScheduler snapshotScheduler;


    //스냅샷에 이미지 등록하기
    @Transactional
    public void registerSnapshotImage(LocalDate day, String name, SnapshotImageRequest snapshotImageRequest) {
        User user = getUserWithFamily(name);

        SnapshotImage snapshotImage = snapshotImageRepository.findByUserAndDate(user, day)
                .orElseThrow(() -> new CustomException(ExceptionCode.SNAPSHOT_NOT_FOUND));

        snapshotImage.updateImage(snapshotImageRequest.imageUrl());
    }

    //스냅샷 전체 그룹 만들기 (개인별 스냅샷도 이때 함께 생성됨.)
    public void createSnapshotGroup(LocalDate day, String name) {
        User user = getUserWithFamily(name);

        Snapshot savedSnapshot = snapshotRepository.save(
                new Snapshot(user.getFamily(), titleService.getTitle(day), day));

        List<User> familyMembers = userRepository.findAllByFamily(user.getFamily());

        familyMembers.forEach((familyMember) -> {
            SnapshotImage snapshotImage = new SnapshotImage(savedSnapshot, familyMember, day);
            snapshotImageRepository.save(snapshotImage);
        });
    }

    //스냅샷 페이지 조회
    public Page<SnapshotResponse> provideSnapshotPage(LocalDate day, Pageable pageable, String name) {
        User user = getUserWithFamily(name);
        return snapshotRepository.findAllByFamilyAndDateBefore(user.getFamily(), day, pageable)
                .map(SnapshotResponse::new);
    }

    //특정 날짜 스냅샷 조회
    public SnapshotResponse provideSnapshot(LocalDate day, String name) {
        User user = getUserWithFamily(name);
        Snapshot snapshot = snapshotRepository.findByFamilyAndDate(user.getFamily(), day)
                .orElseThrow(() -> new CustomException(ExceptionCode.SNAPSHOT_NOT_FOUND));

        return new SnapshotResponse(snapshot);
    }

    @Transactional
    public void scheduleSnapshotAlarm(String name, String time) {
        LocalTime targetTime = LocalTime.parse(time);
        User user = getUserWithFamily(name);
        user.getFamily().registerSnapshotAlarmTime(targetTime);
        snapshotScheduler.scheduleSnapshotAlarm(time);
    }


    //유저 값 가져오기
    private User getUserWithFamily(String name) {
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
        if (user.getFamily() == null) {
            throw new CustomException(ExceptionCode.FAMILY_NOT_FOUND);
        }

        return user;
    }


    public LocalTime getSnapshotAlarmTime(String name) {
        User user = getUserWithFamily(name);
        return user.getFamily().getSnapshotAlarmTime();
    }
}

