package com.likelion13.lucaus_api.dto.response.booth;

import com.likelion13.lucaus_api.enums.BoothReviewEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoothReviewResponseDto {

    private BoothReviewEnum boothReviewTag; // 태그 이름

    private Integer likeNum;// 태그별 좋아요 수
}
