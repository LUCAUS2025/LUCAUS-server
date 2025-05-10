package com.likelion13.lucaus_api.enums;

import lombok.Getter;

@Getter
public enum BoothReviewEnum {
    RECOMMEND("완전 추천해요"),
    FUN("분위기가 재밌어요"),
    BENEFICIAL("콘텐츠가 유익해요"),
    DELICIOUS("간식이 맛있어요");

    private final String koreanName;

    BoothReviewEnum(String koreanName) {this.koreanName = koreanName;}
}
