package com.pinu.familing.domain.snapshot.repository;

import com.pinu.familing.domain.snapshot.entity.SnapshotUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SnapshotUserRepository extends JpaRepository<SnapshotUser, Long> {
}
