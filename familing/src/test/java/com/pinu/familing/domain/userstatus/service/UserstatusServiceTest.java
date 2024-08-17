package com.pinu.familing.domain.userstatus.service;
    
import com.pinu.familing.IntegrationTestSupport;
import com.pinu.familing.domain.userstatus.entity.Userstatus;
import com.pinu.familing.domain.userstatus.repository.UserstatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserstatusServiceTest extends IntegrationTestSupport {

    private final UserstatusService userstatusService;
    private final UserstatusRepository userstatusRepository;

    @Autowired
    UserstatusServiceTest(UserstatusService userstatusService, UserstatusRepository userstatusRepository) {
        this.userstatusService = userstatusService;
        this.userstatusRepository = userstatusRepository;
    }

    @BeforeEach
    public void setUp() {
        userstatusRepository.save(Userstatus.builder().text("공부 중").build());
        userstatusRepository.save(Userstatus.builder().text("노는 중").build());
        userstatusRepository.save(Userstatus.builder().text("쉬는 중").build());
        userstatusRepository.save(Userstatus.builder().text("일하는 중").build());
    }

    @Test
    @DisplayName("상태리스트조회 메서드 테스트")
    public void getUserstatusListTest() {
        //give
        //when
        List<?> userstatusList =userstatusService.getUserstatusList();
        //then
        assertThat(userstatusList.size()).isEqualTo(4);
        System.out.println("userstatusList = " + userstatusList);
    }

}