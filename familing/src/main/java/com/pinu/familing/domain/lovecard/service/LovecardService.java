package com.pinu.familing.domain.lovecard.service;

import com.pinu.familing.domain.lovecard.dto.LovecardResponse;
import com.pinu.familing.domain.lovecard.repository.LovecardLogRepository;
import com.pinu.familing.domain.lovecard.repository.LovecardRepository;
import com.pinu.familing.domain.lovecard.dto.LovecardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LovecardService {

    private final LovecardLogRepository lovecardLogRepository;
    private final LovecardRepository lovecardRepository;

    public Page<?> getLovecardPage(Pageable pageable) {
        Page<LovecardResponse> lovecardResponsePage= lovecardRepository.findAll(pageable)
                .map(LovecardResponse::new);
        return lovecardResponsePage;
    }

    public Page<?> getLovecardByFamilyLogPage(String username, String familyUsername, Pageable pageable) {
        return null;
    }

    public void sendLoveCardToFamily(String name, String familyUsername, LovecardRequest lovecardRequest) {
    }
}
