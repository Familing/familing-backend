package com.pinu.familing.domain.user.entity;

import com.pinu.familing.domain.BaseEntity;
import com.pinu.familing.domain.family.entity.Family;
import com.pinu.familing.domain.user.Gender;
import com.pinu.familing.domain.user.dto.ImageUrl;
import com.pinu.familing.domain.user.dto.Nickname;
import com.pinu.familing.domain.user.dto.Realname;
import com.pinu.familing.global.error.CustomException;
import com.pinu.familing.global.error.ExceptionCode;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_tb")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 유저 아이디로 사용한다.
    private String username;
    // 유저 닉네임 <- 가족에서 사용할 이름
    private String nickname;
    // 유저의 실제 이흠
    private String realname;
    //프로필
    private String imageUrl;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;

    private int age;

    private String role;

    @Builder
    private User(String username, String nickname, String realname, String imageUrl, String role, int age, Gender gender, Family family) {
        this.username = username;
        this.nickname = nickname;
        this.realname = realname;
        this.imageUrl = imageUrl;
        this.role = role;
        this.age = age;
        this.gender = gender;
        this.family = family;
    }

    public void registerFamily(Family family) {
        if (this.family != null) {
            throw new CustomException(ExceptionCode.ALREADY_HAVE_FAMILY);
        }
        this.family = family;
    }

    public void updateNickname(Nickname nickname) {
        this.nickname = nickname.nickname();
    }

    public void updateRealname(Realname realname) {
        this.realname = realname.realname();
    }

    public void updateImageUrl(ImageUrl imageUrl) {
        this.imageUrl = imageUrl.imageUrl();
    }
}
