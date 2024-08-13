package com.pinu.familing.domain.lovecard.service;

import com.pinu.familing.IntegrationTestSupport;
import com.pinu.familing.domain.family.entity.Family;
import com.pinu.familing.domain.family.handler.FamilyCodeHandler;
import com.pinu.familing.domain.lovecard.dto.LovecardRequest;
import com.pinu.familing.domain.lovecard.repository.LovecardLogRepository;
import com.pinu.familing.domain.lovecard.repository.LovecardRepository;
import com.pinu.familing.domain.snapshot.dto.CustomPage;
import com.pinu.familing.domain.user.entity.User;
import com.pinu.familing.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

//@SpringBootTest
class LovecardServiceTest extends IntegrationTestSupport {

    @Autowired
    private LovecardService lovecardService;
    @Autowired
    private LovecardLogRepository lovecardLogRepository;
    @Autowired
    private LovecardRepository lovecardRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void getLovecardPage() {
        //given
        Pageable pageable = PageRequest.of(0, 12);
        //when
        Page<?> page = lovecardService.getLovecardPage(pageable);

        //then
        assertThat(page.getContent()).isNotNull();

        CustomPage customPage = new CustomPage(page);
        System.out.println("customPage = " + customPage);
    }

    @Test
    void getLovecardByFamilyLogPage() {
    }

    @Test
    @Transactional
    void sendLoveCardToFamily() {
        //given
        User user1 = userRepository.save(User.builder()
                .username("user1")
                .realname("서현")
                .nickname("동생")
                .build());

        User user2 = userRepository.save(User.builder()
                .username("user2")
                .realname("서경")
                .nickname("언니")
                .build());


        //when
        lovecardService.sendLoveCardToFamily(user1.getUsername(), user2.getUsername(), new LovecardRequest(1L));

        //then
        assertThat(lovecardLogRepository.findById(1L).get().getSender().getUsername()).isEqualTo("user1");


    }
}