package com.likelion13.lucaus_api.enums;

import lombok.Getter;

@Getter
public enum BoothCategoryEnum {
    SALE("물품 판매"),
    CONCERT("소 연주회"),
    FOOD_SALE("음식 판매"),
    EVENT_GAME("이벤트 / 게임"),
    EXHIBITION("전시회"),
    EXPERIENCE("체험 / 홍보 / 모집"),
    CAMPAIGN("캠페인"),
    STUDENT_ACTIVITY("학생 교류 행사 및 학생회 사업");

    private final String koreanName;

    BoothCategoryEnum(String koreanName) {this.koreanName = koreanName;}
}
