package com.pinu.familing.domain.snapshot.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pinu.familing.domain.snapshot.entity.Snapshot;
import com.pinu.familing.domain.snapshot.entity.SnapshotImage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SnapshotResponse(String Title,
                               LocalDate date,
                               List<UserSnapshot> SnapshotImageList) {

    public SnapshotResponse(Snapshot snapshot) {
        this(
                snapshot.getSnapshotTitle().getTitle(),
                snapshot.getDate(),
                snapshot.getSnapshotImages().stream()
                        .map(UserSnapshot::new)
                        .collect(Collectors.toList())
        );
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    record UserSnapshot(String username,
                                   String nickname,
                                   String userProfile,
                                   String snapshotImage) {

        public UserSnapshot(SnapshotImage snapshotImage) {
            this(snapshotImage.getUser().getUsername(), snapshotImage.getUser().getNickname(), snapshotImage.getUser().getImageUrl(), snapshotImage.getImageUrl());
        }
    }


}

