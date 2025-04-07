package com.likelion13.lucaus_api.dto.request;

import com.likelion13.lucaus_api.enums.BoothReviewEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoothReviewRequestDto {
    private BoothReviewEnum boothReviewTag;
}
