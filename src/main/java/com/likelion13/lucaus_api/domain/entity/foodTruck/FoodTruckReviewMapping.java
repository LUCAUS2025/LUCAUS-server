package com.likelion13.lucaus_api.domain.entity.foodTruck;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FoodTruckReviewMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer likeNum; // 태그별 좋아요 수

    @ManyToOne
    @JoinColumn
    private FoodTruckReview foodTruckReview;

    @ManyToOne
    @JoinColumn
    private FoodTruck foodTruck;

    public void addLikeNum(){
        this.likeNum++;
    }
}
