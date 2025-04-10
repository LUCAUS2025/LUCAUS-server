package com.likelion13.lucaus_api.service.foodTruck;

import com.likelion13.lucaus_api.dto.response.FoodTruck.FoodTruckListByDateResponseDto;

import java.util.List;

public interface FoodTruckOpDateService {
    List<FoodTruckListByDateResponseDto> getFoodTruckListByDate(Integer opDate);
}
