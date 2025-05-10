package com.likelion13.lucaus_api.domain.entity.foodTruck;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FoodTruck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 푸드트럭 이름

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String cover; // 커버 이미지

    @OneToMany(mappedBy = "foodTruck")
    private List<FoodTruckMenu> foodTruckMenu;

    @OneToMany(mappedBy = "foodTruck")
    private List<FoodTruckReviewMapping> foodTruckReviewMappings;

    // 운영일자별 부스 세부정보 테이블과 매핑
    @OneToMany(mappedBy = "foodTruck")
    private List<OpDateFoodTruck> opDateFoodTrucks;
}
