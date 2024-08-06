package com.pinu.familing.domain.family.repository;

import com.pinu.familing.domain.family.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FamilyRepository extends JpaRepository<Family, Long> {
    boolean existsByCode(String code);

    Optional<Family> findByCode(String code);
}
