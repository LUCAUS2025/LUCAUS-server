package com.likelion13.lucaus_api.enums;

import lombok.Getter;

@Getter
public enum BoothCategoryEnum {
    GAME("게임"),
    SOGAETING("소개팅"),
    FOOD("음식"),
    COUNCIL("총학생회");

    private final String koreanName;

    BoothCategoryEnum(String koreanName) {this.koreanName = koreanName;}
}
