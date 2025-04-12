package com.likelion13.lucaus_api.service.booth;

import com.likelion13.lucaus_api.common.exception.ErrorCode;
import com.likelion13.lucaus_api.common.exception.GeneralHandler;
import com.likelion13.lucaus_api.domain.entity.booth.Booth;
import com.likelion13.lucaus_api.domain.entity.booth.BoothReviewMapping;
import com.likelion13.lucaus_api.domain.repository.booth.BoothRepository;
import com.likelion13.lucaus_api.dto.request.BoothReviewRequestDto;
import com.likelion13.lucaus_api.enums.BoothReviewEnum;
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
public class BoothReviewServiceImpl implements BoothReviewService {
    private final BoothRepository boothRepository;

    @Transactional
    public String postBoothReview(Long boothId, BoothReviewRequestDto reviewRequest) {

        if (!isValidTime()) {
            throw new GeneralHandler(ErrorCode.INVALID_REVIEW_TIME);
        }
        Booth booth = boothRepository.findById(boothId).orElse(null);

        if(booth == null) {
            throw new GeneralHandler(ErrorCode.NOT_FOUND_BOOTH);
        }

        List<BoothReviewEnum> reviewTags = reviewRequest.getBoothReviewTags();

        if(reviewTags.isEmpty()) {
            throw new GeneralHandler(ErrorCode.INVALID_REVIEW_TAG);
        }

        reviewTags.forEach(reviewTag -> {
            BoothReviewMapping reviewMapping = booth.getBoothReviewMappings().stream()
                    .filter(data -> data.getBoothReview().getBoothReviewTag().equals(reviewTag))
                    .findFirst().orElse(null);

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
        LocalTime endMonTueWed = LocalTime.of(18, 0); // 월화수: 오후 6시
        LocalTime endThuFri = LocalTime.of(14, 0); // 목금: 오후 2시

        return switch (currentDay) {
            case MONDAY, TUESDAY, WEDNESDAY -> currentTime.isAfter(start) && currentTime.isBefore(endMonTueWed);
            case THURSDAY, FRIDAY -> currentTime.isAfter(start) && currentTime.isBefore(endThuFri);
            default -> true; // 주말x
        };
    }
}
