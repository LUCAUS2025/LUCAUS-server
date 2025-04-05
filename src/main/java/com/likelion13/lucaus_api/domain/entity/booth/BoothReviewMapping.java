package com.likelion13.lucaus_api.domain.entity.booth;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoothReviewMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private BoothReview boothReview;

    @ManyToOne
    @JoinColumn
    private Booth booth;

    private Integer likeNum; // 태그별 좋아요 수
}
