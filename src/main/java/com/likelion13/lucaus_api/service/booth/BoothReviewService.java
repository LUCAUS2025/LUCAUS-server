package com.likelion13.lucaus_api.service.booth;

import com.likelion13.lucaus_api.dto.request.BoothReviewRequestDto;

public interface BoothReviewService {
    String postBoothReview(Long boothId, BoothReviewRequestDto reviewRequest);
}
