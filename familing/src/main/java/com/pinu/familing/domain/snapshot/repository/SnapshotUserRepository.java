package com.pinu.familing.domain.snapshot.repository;

import com.pinu.familing.domain.snapshot.entity.Snapshot;
import com.pinu.familing.domain.snapshot.entity.SnapshotUser;
import com.pinu.familing.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;


public interface SnapshotUserRepository extends JpaRepository<SnapshotUser, Long> {
    Optional<SnapshotUser> findByUserAndDate(User user, LocalDate day);
}
