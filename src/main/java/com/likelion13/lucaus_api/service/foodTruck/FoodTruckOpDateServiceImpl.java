package com.likelion13.lucaus_api.service.foodTruck;

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
        List<Object[]> foodTruckResults = opDateFoodTruckRepository.findFoodTruckListByOpDate(opDate);

        List<Long> foodTruckIds = foodTruckResults.stream()
                .map(row -> ((Number) row[1]).longValue())
                .toList();

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

        return foodTruckResults.stream().map(row -> {
            Integer dayFoodTruckNum = (Integer) row[0];
            Long foodTruckId = (Long) row[1];
            String name = (String) row[2];
            List<String> representMenu = menusMap.getOrDefault(foodTruckId, List.of());

            return new FoodTruckListByDateResponseDto(dayFoodTruckNum, name, representMenu);
        }).collect(Collectors.toList());

    }



}
