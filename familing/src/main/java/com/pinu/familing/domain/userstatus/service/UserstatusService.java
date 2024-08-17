package com.pinu.familing.domain.userstatus.service;

import com.pinu.familing.domain.userstatus.dto.UserstatusResponse;
import com.pinu.familing.domain.userstatus.repository.UserstatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserstatusService {

    public final UserstatusRepository userstatusRepository;

    //상태 리스트 조회
    public List<UserstatusResponse> getUserstatusList() {
        return userstatusRepository.findAll().stream().map(UserstatusResponse::new)
                .collect(Collectors.toList());
    }
}
