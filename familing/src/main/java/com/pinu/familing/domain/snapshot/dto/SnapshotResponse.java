package com.pinu.familing.domain.snapshot.dto;

import com.pinu.familing.domain.snapshot.entity.Snapshot;
import com.pinu.familing.domain.snapshot.entity.SnapshotUser;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record SnapshotResponse(String Title,
                               LocalDate date,
                               List<IndividualSnapshotPhoto> individualSnapshotPhotos) {

    record IndividualSnapshotPhoto(String username,
                                   String nickname,
                                   String image) {

        public IndividualSnapshotPhoto(SnapshotUser snapshotUser) {
            this(snapshotUser.getUser().getUsername(), snapshotUser.getUser().getNickname(), snapshotUser.getImageUrl());
        }
    }

    public SnapshotResponse(Snapshot snapshot) {
        this(
                snapshot.getSnapshotTitle().getTitle(),
                snapshot.getDate(),
                snapshot.getSnapshotUsers().stream()
                        .map(IndividualSnapshotPhoto::new)
                        .collect(Collectors.toList())
        );
    }




}

