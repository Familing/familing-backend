package com.pinu.familing.domain.snapshot.repository;

import com.pinu.familing.domain.family.entity.Family;
import com.pinu.familing.domain.snapshot.entity.Snapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface SnapshotRepository extends JpaRepository<Snapshot, Long> {
    boolean existsByFamilyAndDate(Family family, LocalDate date);
    Optional<Snapshot> findByFamilyAndDate(Family family, LocalDate day);
}
