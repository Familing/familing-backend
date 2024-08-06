package com.pinu.familing.domain.snapshot.entity;

import com.pinu.familing.domain.BaseEntity;
import com.pinu.familing.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SnapshotImage  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "snapshot_group_id",nullable = false)
    private SnapshotGroup snapshotGroup;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(nullable = false)
    private String imageUrl;


}
