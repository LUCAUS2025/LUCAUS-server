package com.likelion13.lucaus_api.domain.entity.foodTruck;

import com.likelion13.lucaus_api.enums.FoodTruckReviewEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FoodTruckReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FoodTruckReviewEnum foodTruckReviewTag; // 태그 이름

    @OneToMany(mappedBy = "foodTruckReview")
    private List<FoodTruckReviewMapping> foodTruckReviewMappings;
}
