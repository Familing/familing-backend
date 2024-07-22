package com.pinu.familing.domain.user.service;

import com.pinu.familing.domain.family.entity.Family;
import com.pinu.familing.domain.family.repositiry.FamilyRepository;
import com.pinu.familing.domain.user.entity.User;
import com.pinu.familing.domain.user.repository.UserRepository;
import com.pinu.familing.global.error.CustomException;
import com.pinu.familing.global.error.ExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;

    public UserService(UserRepository userRepository, FamilyRepository familyRepository) {
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
    }


    @Transactional
    public void addFamilyToUser(String userName, String code) {
        User user = userRepository.findByUsername(userName);
        //코드가 존재하지 않는 경우 (사용자가 잘못된 코드)
        Family family = familyRepository.findByCode(code)
                .orElseThrow(()-> new CustomException(ExceptionCode.INVALID_CODE));

        //내부에 예외 처리 부분 넣어놨습니다.
        user.registerFamily(family);
    }
}
