package com.likelion13.lucaus_api.service.foodTruck;

import com.likelion13.lucaus_api.dto.response.FoodTruck.FoodTruckDetailResponseDto;

import java.util.List;

public interface FoodTruckDetailService {
    List<FoodTruckDetailResponseDto> getFoodTruckDetailByOpDateAndDayFoodTruckNum(Integer opDate, Integer dayFoodTruckNum);
}
