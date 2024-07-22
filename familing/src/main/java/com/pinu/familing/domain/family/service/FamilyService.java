package com.pinu.familing.domain.family.service;

import com.pinu.familing.domain.family.entity.Family;
import com.pinu.familing.domain.family.repositiry.FamilyRepository;
import com.pinu.familing.domain.user.entity.User;
import com.pinu.familing.domain.user.repository.UserRepository;
import com.pinu.familing.global.error.CustomException;
import com.pinu.familing.global.error.ExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class FamilyService {

    private static final char[] BASE_62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;

    public FamilyService(UserRepository userRepository, FamilyRepository familyRepository) {
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
    }

    //가족 만들기
    @Transactional
    public String createFamily(String userName){
        String code = createCode(userName);
        String validCode = validFamilyCode(code);
        //준형님 제가 요즘 도메인에 로직을 담는게 좋다는 이야기를 들었어요. 저도 그렇게 생각하는데.. 흠
        Family savefamily = familyRepository.save(new Family(validCode));
        return savefamily.getCode();
    }


    public String validFamilyCode(String code) {
        if (familyRepository.existsByCode(code)) {
            throw new CustomException(ExceptionCode.INVALID_CODE);
        }
        return code;
    }


    private String createCode(String name) {
        StringBuilder sb = new StringBuilder();

        // SHA-256 해시 함수를 사용하여 입력값을 해시
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException(ExceptionCode.FAILED_CODE_GENERATION);
        }
        byte[] hash = md.digest(name.getBytes());

        // 해시 값을 Base62로 인코딩
        for (byte b : hash) {
            sb.append(BASE_62_CHARS[((b & 0xFF) % BASE_62_CHARS.length)]);
        }
        return sb.toString();
    }

}
