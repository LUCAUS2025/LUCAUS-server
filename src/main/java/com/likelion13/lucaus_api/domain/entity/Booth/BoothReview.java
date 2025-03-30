package com.likelion13.lucaus_api.domain.entity.Booth;

import com.likelion13.lucaus_api.enums.BoothReviewEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoothReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer likeNum; // 태그별 좋아요 수

    @Enumerated(EnumType.STRING)
    private BoothReviewEnum boothReviewTag; // 태그 이름

    // 부스 테이블과 연관관계
    @ManyToOne
    @JoinColumn
    private Booth booth;
}
