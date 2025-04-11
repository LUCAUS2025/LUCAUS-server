package com.likelion13.lucaus_api.controller;

import com.likelion13.lucaus_api.common.response.ApiResponse;
import com.likelion13.lucaus_api.dto.request.FoodTruckReviewRequestDto;
import com.likelion13.lucaus_api.dto.response.FoodTruck.FoodTruckDetailResponseDto;
import com.likelion13.lucaus_api.dto.response.FoodTruck.FoodTruckListByDateResponseDto;
import com.likelion13.lucaus_api.dto.response.booth.BoothDetailResponseDto;
import com.likelion13.lucaus_api.service.foodTruck.FoodTruckDetailService;
import com.likelion13.lucaus_api.service.foodTruck.FoodTruckOpDateService;
import com.likelion13.lucaus_api.service.foodTruck.FoodTruckReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food-truck")
@RequiredArgsConstructor
public class FoodTruckController {

    private final FoodTruckOpDateService foodTruckOpDateService;

    private final FoodTruckDetailService foodTruckDetailService;

    private final FoodTruckReviewService foodTruckReviewService;

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

    @PostMapping("/review/{foodTruckId}")
    public ApiResponse<String> postFoodTruckReview(@PathVariable Long foodTruckId, @RequestBody FoodTruckReviewRequestDto reviewRequest){
        String result = foodTruckReviewService.postFoodTruckReview(foodTruckId, reviewRequest);
        return ApiResponse.onSuccess(result);
    }
}
