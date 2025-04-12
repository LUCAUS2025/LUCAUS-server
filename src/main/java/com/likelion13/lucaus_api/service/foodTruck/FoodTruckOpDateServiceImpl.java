package com.likelion13.lucaus_api.service.foodTruck;

import com.likelion13.lucaus_api.common.exception.ErrorCode;
import com.likelion13.lucaus_api.common.exception.GeneralHandler;
import com.likelion13.lucaus_api.domain.repository.foodTruck.OpDateFoodTruckRepository;
import com.likelion13.lucaus_api.dto.response.FoodTruck.FoodTruckListByDateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodTruckOpDateServiceImpl implements FoodTruckOpDateService {

    private final OpDateFoodTruckRepository opDateFoodTruckRepository;

    public List<FoodTruckListByDateResponseDto> getFoodTruckListByDate(Integer opDate) {

        // 운영일자 잘못된 경우
        if(!(opDate >=19 && opDate <=23)) {
            throw new GeneralHandler(ErrorCode.INVALID_OP_DATE);
        }

        // 푸드트럭 정보 조회
        List<Object[]> foodTruckResults = opDateFoodTruckRepository.findFoodTruckListByOpDate(opDate);

        // 조회된 푸드트럭 없는 경우
        if(foodTruckResults.isEmpty()) {
            throw new GeneralHandler(ErrorCode.NOT_FOUND_FOOD_TRUCK);
        }

        // 조회된 푸드트럭 id들 모아서 리스트로 변환
        List<Long> foodTruckIds = foodTruckResults.stream()
                .map(row -> ((Number) row[1]).longValue())
                .toList();

        // 메뉴 정보 조회
        Map<Long, List<String>> menusMap = new HashMap<>();
        if(!foodTruckIds.isEmpty()) {
            List<Object[]> menuResults = opDateFoodTruckRepository.findRepresentMenusByFoodTruckId(foodTruckIds);

            for(Object[] row : menuResults) {
                Long foodTruckId = (Long) row[0];
                String menusStr = (String) row[1];
                List<String> menus = List.of(menusStr.split(","));
                menusMap.put(foodTruckId, menus);
            }
        }

        // 완전 추천해요 리뷰 숫자 조회
        Map<Long, Integer> recommendMap = new HashMap<>();
        if (!foodTruckIds.isEmpty()) {
            List<Object[]> recommendResults = opDateFoodTruckRepository.findRecommendByFoodTruckIds(foodTruckIds);

            for(Object[] row : recommendResults) {
                Long foodTruckId = (Long) row[0];
                Integer recommendNum = (Integer) row[1];
                recommendMap.put(foodTruckId, recommendNum);
            }
        }

        // 결과 DTO로 변환
        return foodTruckResults.stream().map(row -> {
            Integer dayFoodTruckNum = (Integer) row[0];
            Long foodTruckId = ((Number) row[1]).longValue();
            String name = (String) row[2];
            List<String> representMenu = menusMap.getOrDefault(foodTruckId, List.of());
            Integer recommendNum = recommendMap.get(foodTruckId);

            return new FoodTruckListByDateResponseDto(dayFoodTruckNum, name, representMenu, recommendNum);
        }).collect(Collectors.toList());

    }



}
