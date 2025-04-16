package com.likelion13.lucaus_api.domain.entity.stemp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id; // 아이디 // 수정테스트

    private String pw; // 비밀번호

    private String name; // 이름

    private String studentId; // 학번

    @OneToMany(mappedBy = "user")
    private Set<StampBoard> stampBoards;
}
