package com.likelion13.lucaus_api.dto.response.FoodTruck;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class FoodTruckDetailResponseDto {
    private Integer dayFoodTruckNum; // 날짜별 푸드트럭 번호

    private String name; // 푸드트럭 이름

    private String cover; // 푸드트럭 커버 이미지

    private String location;// 위치

    private List<Map<String, Integer>> menus; // 메뉴들(메뉴이름, 가격)

    private List<Map<String, Integer>> foodTruckReviews; // 푸드트럭리뷰

    private Long foodTruckId;
}
