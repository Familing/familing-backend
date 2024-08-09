package com.pinu.familing.domain.snapshot.dto;

import com.pinu.familing.domain.snapshot.entity.Snapshot;
import com.pinu.familing.domain.snapshot.entity.SnapshotPhoto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record SnapshotResponse(String Title,
                               LocalDate date,
                               List<IndividualSnapshotPhoto> individualSnapshotPhotos) {

    record IndividualSnapshotPhoto(String username,
                                   String nickname,
                                   String image) {

        public IndividualSnapshotPhoto(SnapshotPhoto snapshotPhoto) {
            this(snapshotPhoto.getUser().getUsername(), snapshotPhoto.getUser().getNickname(), snapshotPhoto.getImageUrl());
        }
    }

    public SnapshotResponse(Snapshot snapshot) {
        this(
                snapshot.getSnapshotTitle().getTitle(),
                snapshot.getDate(),
                snapshot.getSnapshotPhotos().stream()
                        .map(IndividualSnapshotPhoto::new)
                        .collect(Collectors.toList())
        );
    }




}

