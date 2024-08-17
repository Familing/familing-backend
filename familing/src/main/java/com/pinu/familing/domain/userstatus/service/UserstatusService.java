package com.pinu.familing.domain.userstatus.service;

import com.pinu.familing.domain.user.entity.User;
import com.pinu.familing.domain.user.repository.UserRepository;
import com.pinu.familing.domain.userstatus.dto.UserstatusRequest;
import com.pinu.familing.domain.userstatus.dto.UserstatusResponse;
import com.pinu.familing.domain.userstatus.entity.Userstatus;
import com.pinu.familing.domain.userstatus.repository.UserstatusRepository;
import com.pinu.familing.global.error.CustomException;
import com.pinu.familing.global.error.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserstatusService {

    public final UserRepository userRepository;
    public final UserstatusRepository userstatusRepository;

    //상태 리스트 조회
    public List<UserstatusResponse> getUserstatusList() {
        return userstatusRepository.findAll().stream().map(UserstatusResponse::new)
                .collect(Collectors.toList());
    }

    public void changeUserstatus(String username, UserstatusRequest userstatusRequest) {
        User user = getUserWithFamily(username);
        Userstatus userstatus = userstatusRepository.findById(userstatusRequest.id())
                .orElseThrow(() -> new CustomException(ExceptionCode.USERSTATUS_NOT_FOUND));

        user.changeStatus(userstatus);

        userRepository.save(user);

    }

    private User getUserWithFamily(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
        if (user.getFamily() == null) {
            throw new CustomException(ExceptionCode.FAMILY_NOT_FOUND);
        }
        return user;
    }
}
