package com.pinu.familing.domain.family.entity;

import com.pinu.familing.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/*
 * 가족 그룹 생성을 언제 할까요?!
 */
@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String familyName;

    @Column(nullable = false, unique = true)
    private String code;

    @Builder.Default
    @OneToMany(mappedBy = "family")
    private List<User> users = new ArrayList<>();

}
