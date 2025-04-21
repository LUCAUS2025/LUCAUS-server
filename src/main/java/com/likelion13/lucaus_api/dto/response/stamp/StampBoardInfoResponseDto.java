package com.likelion13.lucaus_api.dto.response.stamp;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StampBoardInfoResponseDto {
    private Long id; // 도장판 id

    private Integer type; // 도장판 타입

    private Boolean firstReward; // 1차 수령
    private Boolean secondReward;// 2차 수령
    private Boolean thirdReward; // 3차 수령

    private List<StampBoothClearResponseDto> isBoothClear;

}
