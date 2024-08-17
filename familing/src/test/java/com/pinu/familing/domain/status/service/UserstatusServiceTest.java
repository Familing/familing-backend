package com.pinu.familing.domain.status.service;
    
import com.pinu.familing.IntegrationTestSupport;
import com.pinu.familing.domain.family.entity.Family;
import com.pinu.familing.domain.family.repository.FamilyRepository;
import com.pinu.familing.domain.user.entity.User;
import com.pinu.familing.domain.user.repository.UserRepository;
import com.pinu.familing.domain.status.dto.StatusRequest;
import com.pinu.familing.domain.status.entity.Status;
import com.pinu.familing.domain.status.repository.StatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StatusServiceTest extends IntegrationTestSupport {

    private final StatusService StatusService;
    private final UserRepository userRepository;
    private final StatusRepository StatusRepository;
    private final FamilyRepository familyRepository;

    @Autowired
    StatusServiceTest(StatusService StatusService,
                          UserRepository userRepository,
                          StatusRepository StatusRepository,
                          FamilyRepository familyRepository) {
        this.StatusService = StatusService;
        this.userRepository = userRepository;
        this.StatusRepository = StatusRepository;
        this.familyRepository = familyRepository;
    }

    @BeforeEach
    public void setUp() {
        StatusRepository.save(Status.builder().text("공부 중").build());
        StatusRepository.save(Status.builder().text("노는 중").build());
        StatusRepository.save(Status.builder().text("쉬는 중").build());
        StatusRepository.save(Status.builder().text("일하는 중").build());

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
    public void getStatusListTest() {
        //give
        //when
        List<?> StatusList =StatusService.getStatusList();
        //then
        assertThat(StatusList.size()).isEqualTo(4);
        System.out.println("StatusList = " + StatusList);
    }

    @Test
    @DisplayName("유저의 상태 변경되는지 테스트")
    public void changeStatusTest() {
        //give
        StatusRequest statusRequest = new StatusRequest(1L);
        //when
        StatusService.changeUserStatus("user1", statusRequest);
        //then
        assertThat(userRepository.findByUsername("user1").get().getStatus().getText()).isEqualTo("공부 중");

    }

}