package com.likelion13.lucaus_api.service.foodTruck;

import com.likelion13.lucaus_api.domain.entity.foodTruck.FoodTruck;
import com.likelion13.lucaus_api.domain.entity.foodTruck.FoodTruckReviewMapping;
import com.likelion13.lucaus_api.domain.repository.foodTruck.FoodTruckRepository;
import com.likelion13.lucaus_api.dto.request.FoodTruckReviewRequestDto;
import com.likelion13.lucaus_api.enums.FoodTruckReviewEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodTruckReviewServiceImpl implements FoodTruckReviewService {
    private final FoodTruckRepository foodTruckRepository;

    @Transactional
    public String postFoodTruckReview(Long foodTruckId, FoodTruckReviewRequestDto reviewRequest) {

        if (!isValidTime()){
            throw new IllegalStateException("Invalid time"); // 추후 수정
        }
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId).orElse(null); // 추후 예외처리

        List<FoodTruckReviewEnum> reviewTags = reviewRequest.getFoodTruckReviewTags();

        assert foodTruck != null;
        reviewTags.forEach(reviewTag -> {
            FoodTruckReviewMapping reviewMapping = foodTruck.getFoodTruckReviewMappings().stream()
                    .filter(data -> data.getFoodTruckReview().getFoodTruckReviewTag().equals(reviewTag))
                    .findFirst().orElse(null); // 추후 예외처리하기
            assert reviewMapping != null;
            reviewMapping.addLikeNum();
        });

        return "success";
    }

    private boolean isValidTime() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalTime currentTime = now.toLocalTime();
        DayOfWeek currentDay = now.getDayOfWeek();

        LocalTime start = LocalTime.of(10, 0); // 시작 시간: 오전 10시
        LocalTime end = LocalTime.of(18, 0); //

        return switch (currentDay) {
            case MONDAY, TUESDAY, WEDNESDAY ,THURSDAY,FRIDAY -> currentTime.isAfter(start) && currentTime.isBefore(end);
            default -> false; // 주말x
        };
    }
}
