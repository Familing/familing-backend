package com.pinu.familing.global.oauth.service;

import com.pinu.familing.domain.user.entity.User;
import com.pinu.familing.domain.user.repository.UserRepository;
import com.pinu.familing.global.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PrincipalService principalService;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    public String makeToken(User user) {
        return jwtUtil.createJwt(user.getUsername(), user.getRole(), 60 * 60 * 60 * 60L);
    }

    public boolean isAdmin(User user) {
        return "admin".equals(user.getRole());
    }
}
