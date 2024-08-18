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
                               UserSnapshot me,
                               List<UserSnapshot> family) {

    public SnapshotResponse(String username, Snapshot snapshot) {
        this(
                snapshot.getSnapshotTitle().getTitle(),
                snapshot.getDate(),
                snapshot.getSnapshotImages().stream()
                        .filter(snapshotImage -> snapshotImage.getUser().getUsername().equals(username))
                        .map(UserSnapshot::new)
                        .toList().get(0),
                snapshot.getSnapshotImages().stream()
                        .filter(snapshotImage -> snapshotImage.getUser().getUsername().equals(username))
                        .map(UserSnapshot::new)
                        .collect(Collectors.toList()
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

    /*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MyFamilyStatusResponse(UserStatusResponse me,
                                     List<UserStatusResponse> family) {

    public MyFamilyStatusResponse(User user) {
        this(
                new UserStatusResponse(user)
                ,user.getFamily().getUsers().stream()
                .filter(familyUser -> !familyUser.equals(user)) // user와 동일한 객체는 건너뜀
                .map(UserStatusResponse::new) // UserResponse로 변환
                .collect(Collectors.toList()) // List로 변환
        );
    }


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private record UserStatusResponse(String username,
                                     String nickname,
                                     String imageUrl,
                                     String status) {
        private UserStatusResponse(User user) {
            this(
                    user.getUsername(),
                    user.getNickname(),
                    user.getImageUrl(),
                    user.getStatus().getText()// status가 null인 경우 "EMPTY"로 설정
            );
        }
    }
}
     */

}

