package com.pinu.familing.domain.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserRequest(String nickname,
                          String realname,
                          String imageUrl) {

    public boolean existNickname(){
        return this.nickname != null && !this.nickname.isBlank();
    }

    public boolean existRealname(){
        return this.realname != null && !this.realname.isBlank();
    }

    public boolean existImageUrl(){
        return this.imageUrl != null && !this.imageUrl.isBlank();
    }
}
