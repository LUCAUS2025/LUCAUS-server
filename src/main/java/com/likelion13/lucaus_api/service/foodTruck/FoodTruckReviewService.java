package com.likelion13.lucaus_api.service.foodTruck;

import com.likelion13.lucaus_api.dto.request.FoodTruckReviewRequestDto;

public interface FoodTruckReviewService {
    String postFoodTruckReview(Long foodTruckId, FoodTruckReviewRequestDto reviewRequest);
}
