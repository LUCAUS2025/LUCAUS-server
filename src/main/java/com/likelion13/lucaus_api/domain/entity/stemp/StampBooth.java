package com.likelion13.lucaus_api.domain.entity.stemp;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StampBooth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 부스이름

    private String pw; // 도장 인증 비밀번호

    @OneToMany(mappedBy = "stampBooth")
    private Set<StampBoardBoothMapping> stampBoardBoothMappings;
}
