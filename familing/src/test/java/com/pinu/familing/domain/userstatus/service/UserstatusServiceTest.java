package com.pinu.familing.domain.userstatus.service;
    
import com.pinu.familing.IntegrationTestSupport;
import com.pinu.familing.domain.family.entity.Family;
import com.pinu.familing.domain.family.repository.FamilyRepository;
import com.pinu.familing.domain.user.entity.User;
import com.pinu.familing.domain.user.repository.UserRepository;
import com.pinu.familing.domain.userstatus.dto.UserstatusRequest;
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
    private final UserRepository userRepository;
    private final UserstatusRepository userstatusRepository;
    private final FamilyRepository familyRepository;

    @Autowired
    UserstatusServiceTest(UserstatusService userstatusService,
                          UserRepository userRepository,
                          UserstatusRepository userstatusRepository,
                          FamilyRepository familyRepository) {
        this.userstatusService = userstatusService;
        this.userRepository = userRepository;
        this.userstatusRepository = userstatusRepository;
        this.familyRepository = familyRepository;
    }

    @BeforeEach
    public void setUp() {
        userstatusRepository.save(Userstatus.builder().text("공부 중").build());
        userstatusRepository.save(Userstatus.builder().text("노는 중").build());
        userstatusRepository.save(Userstatus.builder().text("쉬는 중").build());
        userstatusRepository.save(Userstatus.builder().text("일하는 중").build());

        User user1 = userRepository.save(
                User.builder()
                        .username("user1")
                        .nickname("유저1").build());


        User user2 = userRepository.save(
                User.builder()
                        .username("user2")
                        .nickname("유저2").build());

        Family family = familyRepository.save(new Family("가족", "code"));

        user1.registerFamily(family);
        user2.registerFamily(family);

        userRepository.save(user1);
        userRepository.save(user2);


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

    @Test
    @DisplayName("유저의 상태 변경되는지 테스트")
    public void changeUserstatusTest() {
        //give
        UserstatusRequest userstatusRequest = new UserstatusRequest(1L);
        //when
        userstatusService.changeUserstatus("user1", userstatusRequest);
        //then
        assertThat(userRepository.findByUsername("user1").get().getStatus().getText()).isEqualTo("공부 중");

    }

}