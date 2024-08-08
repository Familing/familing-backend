package com.pinu.familing.domain.snapshot.service;

import com.pinu.familing.domain.snapshot.dto.SnapshotImageRequest;
import com.pinu.familing.domain.snapshot.dto.SnapshotResponse;
import com.pinu.familing.domain.snapshot.entity.Snapshot;
import com.pinu.familing.domain.snapshot.entity.SnapshotUser;
import com.pinu.familing.domain.snapshot.entity.SnapshotTitle;
import com.pinu.familing.domain.snapshot.repository.SnapshotRepository;
import com.pinu.familing.domain.snapshot.repository.SnapshotUserRepository;
import com.pinu.familing.domain.user.entity.User;
import com.pinu.familing.domain.user.repository.UserRepository;
import com.pinu.familing.global.error.CustomException;
import com.pinu.familing.global.error.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SnapshotService {

    private final SnapshotUserRepository snapshotUserRepository;
    private final SnapshotRepository snapshotRepository;
    private final UserRepository userRepository;
    private final TitleService titleService;



    //스냅샷에 이미지 등록하기
    public void registerSnapshotImage(LocalDate day, String name, SnapshotImageRequest snapshotImageRequest) {
        User user = getUser(name);
        Snapshot snapshot = snapshotRepository.findByFamilyAndDate(user.getFamily(), day)
                .orElseThrow(() -> new CustomException(ExceptionCode.SNAPSHOT_NOT_FOUND));

        snapshotUserRepository.save(SnapshotUser.builder()
                        .snapshot(snapshot)
                        .user(user)
                        .imageUrl(snapshotImageRequest.imageUrl())
                        .build());

    }

    //패밀리스냅샷만들기
    public void createSnapshotGroup(LocalDate day, String name) {
        User user = getUser(name);
        //이미 존재하는 경우에 대해서는 동작하지 않음
        if (snapshotRepository.existsByFamilyAndDate(user.getFamily(),day)){
            return;
        }

        //해당 날짜에 맞는 주제를 가져오는 로직
        SnapshotTitle snapshotTitle = titleService.getTitle(day);

        snapshotRepository.save(new Snapshot(user.getFamily(), snapshotTitle, day));
    }

    public SnapshotResponse offerSnapshot(LocalDate day, String name) {
        User user = getUser(name);

        Snapshot snapshot =  snapshotRepository.findByFamilyAndDate(user.getFamily(), day)
                .orElseThrow(() -> new CustomException(ExceptionCode.SNAPSHOT_NOT_FOUND));

        return new SnapshotResponse(snapshot);
    }

    //유저값가져오기
    private User getUser(String name) {
        return userRepository.findByUsername(name)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

    }


}

