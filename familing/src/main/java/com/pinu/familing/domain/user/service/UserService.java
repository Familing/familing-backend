package com.pinu.familing.domain.user.service;

import com.pinu.familing.domain.family.repositiry.FamilyRepository;
import com.pinu.familing.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;

    public UserService(UserRepository userRepository, FamilyRepository familyRepository) {
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
    }

}
