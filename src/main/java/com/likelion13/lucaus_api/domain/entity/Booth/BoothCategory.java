package com.likelion13.lucaus_api.domain.entity.Booth;

import com.likelion13.lucaus_api.enums.BoothCategoryEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoothCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BoothCategoryEnum category; // 부스카테고리

    // 부스 테이블과 연관관계
    @ManyToOne
    @JoinColumn
    private Booth booth;
}
