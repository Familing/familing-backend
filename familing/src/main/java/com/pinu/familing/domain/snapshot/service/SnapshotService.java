package com.pinu.familing.domain.snapshot.service;

import com.pinu.familing.domain.family.entity.Family;
import com.pinu.familing.domain.family.repository.FamilyRepository;
import com.pinu.familing.domain.snapshot.dto.SnapshotImageRequest;
import com.pinu.familing.domain.snapshot.dto.SnapshotResponse;
import com.pinu.familing.domain.snapshot.entity.Snapshot;
import com.pinu.familing.domain.snapshot.entity.SnapshotImage;
import com.pinu.familing.domain.snapshot.entity.SnapshotTitle;
import com.pinu.familing.domain.snapshot.repository.SnapshotImageRepository;
import com.pinu.familing.domain.snapshot.repository.SnapshotRepository;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class SnapshotService {

    private final SnapshotImageRepository snapshotImageRepository;
    private final SnapshotRepository snapshotRepository;
    private final UserRepository userRepository;
    private final TitleService titleService;
    private final FamilyRepository familyRepository;


    //스냅샷에 이미지 등록하기
    @Transactional
    public void registerSnapshotImage(LocalDate day, String name, SnapshotImageRequest snapshotImageRequest) {
        User user = getUser(name);

        SnapshotImage snapshotImage = snapshotImageRepository.findByUserAndDate(user,day)
                .orElseThrow(() -> new CustomException(ExceptionCode.SNAPSHOT_NOT_FOUND));

        snapshotImage.updateImage(snapshotImageRequest.imageUrl());
    }


    //스냅샷 엔티티 전체 생성하기
    public void createAllSnapshotEntity() {
        LocalDate currentDate = LocalDate.now();
        List<Family> familyList = familyRepository.findAll();

        familyList.forEach(family -> {
            createSnapshotEntity(family, currentDate);
            List<User> familyMembers = userRepository.findAllByFamily(family);
            familyMembers.forEach(familyMember -> {
                createSnapshotImageEntity(familyMember, currentDate);
            });
        });

    }


    //스냅샷 엔티티 생성하기 (개인별 스냅샷도 이때 함께 생성됨.)
    private void createSnapshotEntity(Family family, LocalDate currentDate) {
        SnapshotTitle title = titleService.getTitle(currentDate);
        System.out.println(family.getFamilyName() + " : 스냅샷 엔티티 생성하기");
        snapshotRepository.save(new Snapshot(family, title, currentDate));
    }

    private void createSnapshotImageEntity(User user, LocalDate currentDate) {
        Snapshot snapshot = snapshotRepository.findByFamilyAndDate(user.getFamily(), currentDate)
                .orElseThrow(() -> new CustomException(ExceptionCode.SNAPSHOT_NOT_FOUND));

        SnapshotImage snapshotImage = new SnapshotImage(snapshot,user, currentDate);
        snapshotImageRepository.save(snapshotImage);
        System.out.println(user.getFamily().getFamilyName() + " : " + user.getNickname() + " 의 엔티티 생성");

    }

    //스냅샷 페이지 조회
    public Page<SnapshotResponse> provideSnapshotPage(LocalDate day, Pageable pageable, String name) {
        Family family = getFamily(name);
        return snapshotRepository.findAllByFamilyAndDateBefore(family, day, pageable)
                .map(SnapshotResponse::new);
    }

    //특정 날짜 스냅샷 조회
    public SnapshotResponse provideSnapshot(LocalDate day, String name) {
        Family family = getFamily(name);
        Snapshot snapshot = snapshotRepository.findByFamilyAndDate(family, day)
                .orElseThrow(() -> new CustomException(ExceptionCode.SNAPSHOT_NOT_FOUND));

        return new SnapshotResponse(snapshot);
    }

    //회원가입이나, 가족 등록시에 호출
    public void createSnapshotDueToFamilyRegistration(String username) {
        Family family = getFamily(username);
        User user = getUser(username);
        LocalDate currentDate = LocalDate.now();

        //스냅샷 저장
        if (!snapshotRepository.existsByFamilyAndDate(family, currentDate)) {
            createSnapshotEntity(family, currentDate);
        }

        //스냅샷 이미지 생성
        createSnapshotImageEntity(user, currentDate);

    }


    //유저 값 가져오기
    private Family getFamily(String name) {
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
        if (user.getFamily() == null) {
            throw new CustomException(ExceptionCode.FAMILY_NOT_FOUND);
        }

        return user.getFamily();
    }

    private User getUser(String name) {
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
        if (user.getFamily() == null) {
            throw new CustomException(ExceptionCode.FAMILY_NOT_FOUND);
        }
        return user;
    }

}

