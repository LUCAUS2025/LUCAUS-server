package com.likelion13.lucaus_api.dto.request;

import com.likelion13.lucaus_api.enums.FoodTruckReviewEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodTruckReviewRequestDto {
    private FoodTruckReviewEnum foodTruckReviewTag;
}
