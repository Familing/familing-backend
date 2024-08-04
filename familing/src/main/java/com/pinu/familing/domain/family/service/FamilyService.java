package com.pinu.familing.domain.family.service;

import com.pinu.familing.domain.family.dto.FamilyDto;
import com.pinu.familing.domain.family.entity.Family;
import com.pinu.familing.domain.family.handler.FamilyCodeHandler;
import com.pinu.familing.domain.family.repositiry.FamilyRepository;
import com.pinu.familing.domain.user.repository.UserRepository;
import com.pinu.familing.global.error.CustomException;
import com.pinu.familing.global.error.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;


    //가족 만들기
    @Transactional
    public FamilyDto registerNewFamily(String userName, String familyName) {
        //코드 발급 시점을 여기가 아니라 밖에서 해도 될 것 같기는 한데..
        String code = FamilyCodeHandler.createCode(userName);
        String validCode = checkValid(code);

        Family family = new Family(familyName, validCode);
        Family savefamily = familyRepository.save(family);

        return new FamilyDto(savefamily);
    }

    private String checkValid(String code) {
        if (familyRepository.existsByCode(code)) {
            throw new CustomException(ExceptionCode.INVALID_CODE);
        }
        return code;
    }

}
