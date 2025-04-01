package com.likelion13.lucaus_api.domain.entity.Booth;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Booth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 부스명

    private String owner; // 부스 운영 주체

    private String info; // 부스 소개글

    private LocalDateTime createdAt; // 생성일

    private LocalDateTime updatedAt; // 수정일

    private String cover; // 커버 이미지

    @OneToMany(mappedBy = "booth")
    private List<BoothCategoryMapping> boothCategoryMappings;

    @OneToMany(mappedBy = "booth")
    private List<BoothReviewMapping> boothReviewMappings;

    // 운영일자별 부스 세부정보 테이블과 매핑
    @OneToMany(mappedBy = "booth")
    private List<OpDateBooth> opDateBooth;
}
