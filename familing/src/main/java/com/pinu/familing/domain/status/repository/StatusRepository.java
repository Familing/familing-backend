package com.pinu.familing.domain.status.repository;

import com.pinu.familing.domain.status.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository  extends JpaRepository<Status, Long> {


}