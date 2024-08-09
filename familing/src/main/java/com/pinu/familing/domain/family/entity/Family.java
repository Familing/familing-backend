package com.pinu.familing.domain.family.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/*
 * 가족 그룹 생성을 언제 할까요?!
 */
@Entity
@Getter
@NoArgsConstructor
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String familyName;

    @Column(nullable = false, unique = true)
    private String code;

    private int membersNum;

    private LocalTime snapshotAlarmTime;

    public Family(String familyName, String code) {
        this.familyName = familyName;
        this.code = code;
        this.membersNum = 0;
        this.snapshotAlarmTime =  LocalTime.of(12, 0);
    }

    public void registerSnapshotAlarmTime(LocalTime time) {
        this.snapshotAlarmTime = time;
    }
    public void addMember() {
        this.membersNum += 1;
    }

}
