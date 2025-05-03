package com.likelion13.lucaus_api.service.foodTruck;

import com.likelion13.lucaus_api.common.exception.ErrorCode;
import com.likelion13.lucaus_api.common.exception.GeneralHandler;
import com.likelion13.lucaus_api.domain.repository.foodTruck.FoodTruckDetailRepository;
import com.likelion13.lucaus_api.dto.response.FoodTruck.FoodTruckDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodTruckDetailServiceImpl implements FoodTruckDetailService {

    private final FoodTruckDetailRepository foodTruckDetailRepository;

    public List<FoodTruckDetailResponseDto> getFoodTruckDetailByOpDateAndDayFoodTruckNum(Integer opDate, Integer dayFoodTruckNum){

        // 운영일자 어긋난 경우
        if(!(opDate >=19 && opDate <=23)) {
            throw new GeneralHandler(ErrorCode.INVALID_OP_DATE);
        }

        List<Object[]> results = foodTruckDetailRepository.findFoodTruckDetailByOpDateAndDayFoodTruckNum(opDate,dayFoodTruckNum);

        // 조건 맞는 푸드트럭 존재하지 않는 경우
        if(results.isEmpty()) {
            throw new GeneralHandler(ErrorCode.NOT_FOUND_FOOD_TRUCK);
        }

        return results.stream().map(row -> {
            Integer dayFoodTruckNumResult = (Integer) row[0];
            Long foodTruckId = ((Number) row[1]).longValue();
            String name = (String) row[2];
            String cover = (String) row[3];
            String location = (String) row[4];

            //메뉴 조회
            List<Object[]> menuResults = foodTruckDetailRepository.findBoothMenu(foodTruckId);
            List<Map<String, Integer>> foodTruckMenuList = new ArrayList<>();
            for (Object[] menuRow : menuResults) {
                String menuName = (String) menuRow[0];
                Integer menuPrice = ((Number) menuRow[1]).intValue();
                Map<String, Integer> menuMap = new HashMap<>();
                menuMap.put(menuName, menuPrice);
                foodTruckMenuList.add(menuMap);
            }

            // 리뷰 조회
            List<Object[]> reviewResults = foodTruckDetailRepository.findFoodTruckReviewsByFoodTruckId(foodTruckId);
            List<Map<String, Integer>> foodTruckReviewList = new ArrayList<>();
            for (Object[] reviewRow : reviewResults) {
                String reviewTag = (String) reviewRow[0];
                Integer likeNum = ((Number) reviewRow[1]).intValue();
                Map<String, Integer> reviewMap = new HashMap<>();
                reviewMap.put(reviewTag, likeNum);
                foodTruckReviewList.add(reviewMap);
            }

            // opDateList 조회
            List<Integer> opDateList = foodTruckDetailRepository.findOpDateListByFoodTruckId(foodTruckId);

            return new FoodTruckDetailResponseDto(
                    dayFoodTruckNum,
                    name,
                    cover,
                    location,
                    foodTruckMenuList,
                    foodTruckReviewList,
                    foodTruckId,
                    opDateList
            );
        }).collect(Collectors.toList());
    }
}
