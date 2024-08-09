package com.pinu.familing.domain.snapshot.entity;

import com.pinu.familing.domain.BaseEntity;
import com.pinu.familing.domain.family.entity.Family;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 protected로 설정
public class Snapshot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;

    @ManyToOne
    @JoinColumn(name = "title_id")
    private SnapshotTitle snapshotTitle;

    private LocalDate date;

    @OneToMany(orphanRemoval = true, mappedBy = "snapshot", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<SnapshotImage> snapshotImages = new ArrayList<>();

    // 특정 생성자에만 @Builder 적용
    @Builder
    public Snapshot(Family family, SnapshotTitle snapshotTitle, LocalDate date) {
        this.family = family;
        this.snapshotTitle = snapshotTitle;
        this.date = date;
        this.snapshotImages = new ArrayList<>();
    }
}
