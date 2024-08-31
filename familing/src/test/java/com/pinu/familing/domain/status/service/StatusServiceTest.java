package com.pinu.familing.domain.status.service;

import com.pinu.familing.IntegrationTestSupport;
import com.pinu.familing.domain.family.entity.Family;
import com.pinu.familing.domain.family.repository.FamilyRepository;
import com.pinu.familing.domain.status.dto.MyFamilyStatusResponse;
import com.pinu.familing.domain.status.dto.StatusResponse;
import com.pinu.familing.domain.user.entity.User;
import com.pinu.familing.domain.user.repository.UserRepository;
import com.pinu.familing.domain.status.dto.StatusRequest;
import com.pinu.familing.domain.status.entity.Status;
import com.pinu.familing.domain.status.repository.StatusRepository;
import jakarta.persistence.EntityManager;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
class StatusServiceTest extends IntegrationTestSupport {

    @Autowired
    private StatusService statusService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        statusRepository.saveAll(List.of(
                Status.builder().text("공부 중").build(),
                Status.builder().text("노는 중").build(),
                Status.builder().text("쉬는 중").build(),
                Status.builder().text("일하는 중").build()
        ));

        Family family = familyRepository.save(new Family("가족", "code"));

        User user1 = userRepository.save(User.builder()
                .username("user1")
                .nickname("유저1")
                .family(family)
                .build());

        User user2 = userRepository.save(User.builder()
                .username("user2")
                .nickname("유저2")
                .family(family)
                .build());

        entityManager.flush();
        entityManager.clear();
    }

    @AfterEach
    public void tearDown() {
        statusRepository.deleteAll();
        userRepository.deleteAll();
        familyRepository.deleteAll();
    }

    @Test
    @DisplayName("상태 리스트 조회 메서드 테스트")
    @Transactional
    void getStatusListTest() {
        List<StatusResponse> statusList = statusService.getStatusList();
        assertThat(statusList).hasSize(4);
    }

    @Test
    @DisplayName("유저의 상태 변경 테스트")
    @Transactional
    void changeStatusTest() {
        StatusRequest statusRequest = new StatusRequest(statusRepository.findByText("쉬는 중").get().getId());
        statusService.changeUserStatus("user1", statusRequest);
        assertThat(userRepository.findByUsername("user1").get().getStatus().getText()).isEqualTo("쉬는 중");
    }

    @Test
    @DisplayName("유저 기준 가족의 상태 조회")
    @Transactional
    void getFamilyStatusListTest() {


        //given
        User user1 = userRepository.findByUsername("user1").get();
        User user2 = userRepository.findByUsername("user2").get();

        StatusRequest statusRequest1 = new StatusRequest(statusRepository.findByText("쉬는 중").get().getId());
        StatusRequest statusRequest2 = new StatusRequest(statusRepository.findByText("노는 중").get().getId());

        entityManager.flush();
        //when
        statusService.changeUserStatus("user1", statusRequest1);
        statusService.changeUserStatus("user2", statusRequest2);
        //then
        MyFamilyStatusResponse response = statusService.getFamilyStatusList("user1");
        assertThat(response.family().get(0).status()).isEqualTo("노는 중");
    }
}