package com.pinu.familing.domain.snapshot.service;

import com.pinu.familing.domain.snapshot.dto.SnapshotImageRequest;
import com.pinu.familing.domain.snapshot.dto.SnapshotResponse;
import com.pinu.familing.domain.snapshot.entity.Snapshot;
import com.pinu.familing.domain.snapshot.entity.SnapshotUser;
import com.pinu.familing.domain.snapshot.repository.SnapshotRepository;
import com.pinu.familing.domain.snapshot.repository.SnapshotUserRepository;
import com.pinu.familing.domain.user.entity.User;
import com.pinu.familing.domain.user.repository.UserRepository;
import com.pinu.familing.global.error.CustomException;
import com.pinu.familing.global.error.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SnapshotService {

    private final SnapshotUserRepository snapshotUserRepository;
    private final SnapshotRepository snapshotRepository;
    private final UserRepository userRepository;
    private final TitleService titleService;


    //스냅샷에 이미지 등록하기
    public void registerSnapshotImage(LocalDate day, String name, SnapshotImageRequest snapshotImageRequest) {
        User user = getUserWithFamily(name);

        SnapshotUser snapshotUser = snapshotUserRepository.findByUserAndDate(user, day)
                .orElseThrow(() -> new CustomException(ExceptionCode.SNAPSHOT_NOT_FOUND));

        snapshotUser.updateImage(snapshotImageRequest.imageUrl());
    }

    //스냅샷 전체 그룹 만들기 (개인별 스냅샷도 이때 함께 생성됨.)
    public void createSnapshotGroup(LocalDate day, String name) {
        User user = getUserWithFamily(name);

        Snapshot savedSnapshot = snapshotRepository.save(
                new Snapshot(user.getFamily(), titleService.getTitle(day), day));

        List<User> familyMembers = userRepository.findAllByFamily(user.getFamily());

        familyMembers.forEach((familyMember) -> {
            SnapshotUser snapshotUser = new SnapshotUser(savedSnapshot, familyMember, day);
            snapshotUserRepository.save(snapshotUser);
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

    //유저 값 가져오기
    private User getUserWithFamily(String name) {
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
        if (user.getFamily() == null) {
            throw new CustomException(ExceptionCode.FAMILY_NOT_FOUND);
        }

        return user;
    }


}

