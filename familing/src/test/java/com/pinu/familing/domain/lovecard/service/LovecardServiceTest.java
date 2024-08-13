package com.pinu.familing.domain.lovecard.service;

import com.pinu.familing.IntegrationTestSupport;
import com.pinu.familing.domain.snapshot.dto.CustomPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

//@SpringBootTest
class LovecardServiceTest extends IntegrationTestSupport {

    @Autowired
    private LovecardService lovecardService;

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
    void sendLoveCardToFamily() {
    }
}