package com.pinu.familing.global.oauth.service;

import com.pinu.familing.domain.user.dto.UserDto;
import com.pinu.familing.domain.user.entity.User;
import com.pinu.familing.domain.user.repository.UserRepository;
import com.pinu.familing.global.oauth.dto.CustomOAuth2User;
import com.pinu.familing.global.oauth.dto.KakaoResponse;
import com.pinu.familing.global.oauth.dto.OAuth2Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    //OAuth 로그인 시 호출된다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }
        else {
            System.out.println("else service");
            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();

        User existUser = userRepository.findByUsername(username);

        if (existUser == null) { // 새로운 유저 생성
            userRepository.save(User.builder()
                    .username(username)
                    .nickname("user")
                    .role("ROLE_USER")
                    .build());
            UserDto userDTO = new UserDto(username, "user", "ROLE_USER");
            return new CustomOAuth2User(userDTO);
        }
        else {
            return new CustomOAuth2User(new UserDto(existUser.getUsername(), existUser.getNickname(), existUser.getRole()));
        }

    }
}