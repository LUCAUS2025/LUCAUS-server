package com.likelion13.lucaus_api.domain.entity.booth;

import com.likelion13.lucaus_api.enums.BoothReviewEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoothReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BoothReviewEnum boothReviewTag; // 태그 이름

    @OneToMany(mappedBy = "boothReview")
    private List<BoothReviewMapping> boothReviewMapping;
}
