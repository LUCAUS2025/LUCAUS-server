package com.likelion13.lucaus_api.enums;

import lombok.Getter;

@Getter
public enum FoodTruckReviewEnum {
    RECOMMEND("완전 추천해요"),
    DELICIOUS("맛있어요"),
    MANY("양이 많아요"),
    FAST("빨라요");

    private final String koreanName;

    FoodTruckReviewEnum(String koreanName) {this.koreanName = koreanName;}
}
