package com.pinu.familing.domain.snapshot.repository;

import com.pinu.familing.domain.snapshot.entity.Snapshot;
import com.pinu.familing.domain.snapshot.entity.SnapshotPhoto;
import com.pinu.familing.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;


public interface SnapshotPhotoRepository extends JpaRepository<SnapshotPhoto, Long> {
    Optional<SnapshotPhoto> findByUserAndDate(User user, LocalDate day);
}
