package com.pinu.familing.domain.snapshot.entity;

import com.pinu.familing.domain.BaseEntity;
import com.pinu.familing.domain.family.entity.Family;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class SnapshotGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;

    @ManyToOne
    @JoinColumn(name = "snapshot_title_id")
    private SnapshotTitle snapshotTitle;


}
