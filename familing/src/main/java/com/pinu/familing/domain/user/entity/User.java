package com.pinu.familing.domain.user.entity;

import com.pinu.familing.domain.BaseEntity;
import com.pinu.familing.domain.user.Gender;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_tb")
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 유저 아이디로 사용한다.
    private String username;
    // 유저 닉네임
    private String nickname;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private int age;

    @Builder
    private User(String username, String nickname, String role, int age, Gender gender) {
        this.username = username;
        this.nickname = nickname;
        this.role = role;
        this.age = age;
        this.gender = gender;
    }

    private String role;

}
