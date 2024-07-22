package com.pinu.familing.domain.family.service;

import com.pinu.familing.domain.family.dto.FamilyCode;
import com.pinu.familing.domain.family.entity.Family;
import com.pinu.familing.domain.family.handler.FamilyCodeHandler;
import com.pinu.familing.domain.family.repositiry.FamilyRepository;
import com.pinu.familing.domain.user.entity.User;
import com.pinu.familing.domain.user.repository.UserRepository;
import com.pinu.familing.global.error.CustomException;
import com.pinu.familing.global.error.ExceptionCode;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class FamilyService {

    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;

    public FamilyService(UserRepository userRepository, FamilyRepository familyRepository) {
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
    }

    //가족 만들기
    @Transactional
    public String registerNewFamily(String userName,String familyName){
        String validCode = validFamilyCode(FamilyCodeHandler.createCode(userName));

        Family family = new Family(familyName,validCode);
        Family savefamily = familyRepository.save(family);

        return savefamily.getCode();
    }

    @Transactional
    public void addFamilyToUser(String userName, String code) {
        User user = userRepository.findByUsername(userName);

        Family family = familyRepository.findByCode(code)
                .orElseThrow(()-> new CustomException(ExceptionCode.INVALID_CODE));

        //내부에 예외 처리 부분 넣어놨습니다.
        user.registerFamily(family);
    }

    private String validFamilyCode(String code) {
        if (familyRepository.existsByCode(code)) {
            throw new CustomException(ExceptionCode.INVALID_CODE);
        }
        return code;
    }

}
