package com.likelion13.lucaus_api.controller;

import com.likelion13.lucaus_api.common.response.ApiResponse;
import com.likelion13.lucaus_api.dto.response.FoodTruck.FoodTruckDetailResponseDto;
import com.likelion13.lucaus_api.dto.response.FoodTruck.FoodTruckListByDateResponseDto;
import com.likelion13.lucaus_api.dto.response.booth.BoothDetailResponseDto;
import com.likelion13.lucaus_api.service.foodTruck.FoodTruckDetailService;
import com.likelion13.lucaus_api.service.foodTruck.FoodTruckOpDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/food-truck")
@RequiredArgsConstructor
public class FoodTruckController {

    private final FoodTruckOpDateService foodTruckOpDateService;

    private final FoodTruckDetailService foodTruckDetailService;

    @GetMapping("/{opDate}")
    public ApiResponse<List<FoodTruckListByDateResponseDto>> getFoodTruckListByDate(@PathVariable Integer opDate){
        List<FoodTruckListByDateResponseDto> result = foodTruckOpDateService.getFoodTruckListByDate(opDate);
        return ApiResponse.onSuccess(result);
    }

    @GetMapping("/{opDate}/{dayFoodTruckNum}")
    public ApiResponse<List<FoodTruckDetailResponseDto>> getFoodTruckDetail(@PathVariable Integer opDate, @PathVariable Integer dayFoodTruckNum){
        List<FoodTruckDetailResponseDto> result = foodTruckDetailService.getFoodTruckDetailByOpDateAndDayFoodTruckNum(opDate, dayFoodTruckNum);
        return ApiResponse.onSuccess(result);
    }
}
