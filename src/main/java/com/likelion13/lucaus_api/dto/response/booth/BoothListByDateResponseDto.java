package com.likelion13.lucaus_api.dto.response.booth;

import com.likelion13.lucaus_api.enums.BoothCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;


@Data
@AllArgsConstructor
public class BoothListByDateResponseDto {
    private Integer dayBoothNum;
    private String name;
    //private String info;
    private String owner;
    private List<String> categories;
    private Integer recommendNum;
}