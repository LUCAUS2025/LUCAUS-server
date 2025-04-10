package com.likelion13.lucaus_api.dto.response.FoodTruck;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FoodTruckListByDateResponseDto {
    private Integer dayBoothNum;
    private String name;
    private List<String> representMenu;
}
