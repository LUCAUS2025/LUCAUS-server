package com.likelion13.lucaus_api.service.booth;

import com.likelion13.lucaus_api.common.exception.ErrorCode;
import com.likelion13.lucaus_api.common.exception.GeneralHandler;
import com.likelion13.lucaus_api.domain.repository.booth.BoothDetailRepository;
import com.likelion13.lucaus_api.dto.response.booth.BoothDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoothDetailServiceImpl implements BoothDetailService {

    private final BoothDetailRepository boothDetailRepository;

    public List<BoothDetailResponseDto> getBoothDetailByOpDateAndDayBoothNum(Integer opDate, Integer dayBoothNum) {

        // 운영일자 어긋난 경우
        if(!(opDate >=19 && opDate <=23)) {
            throw new GeneralHandler(ErrorCode.INVALID_OP_DATE);
        }

        List<Object[]> results = boothDetailRepository.findBoothDetailByOpDateAndDayBoothNum(opDate, dayBoothNum);

        // 조건 맞는 부스 존재하지 않는 경우
        if(results.isEmpty()) {
            throw new GeneralHandler(ErrorCode.NOT_FOUND_BOOTH);
        }

        return results.stream().map(row -> {
            Integer dayBoothNumResult = (Integer) row[0];
            Long boothId = ((Number) row[1]).longValue();
            String name = (String) row[2];
            String owner = (String) row[3];
            String info = (String) row[4];
            String cover = (String) row[5];
            String location = (String) row[6];

            // 카테고리 조회
            List<String> categories = boothDetailRepository.findBoothCategoriesByBoothId(boothId);

            // 리뷰 조회
            List<Object[]> reviewResults = boothDetailRepository.findBoothReviewsByBoothId(boothId);
            List<Map<String, Integer>> boothReviewList = new ArrayList<>();
            for (Object[] reviewRow : reviewResults) {
                String reviewTag = (String) reviewRow[0];
                Integer likeNum = ((Number) reviewRow[1]).intValue();
                Map<String, Integer> reviewMap = new HashMap<>();
                reviewMap.put(reviewTag, likeNum);
                boothReviewList.add(reviewMap);
            }

            // BoothDetailResponseDto 생성
            return new BoothDetailResponseDto(
                    dayBoothNumResult,
                    name,
                    owner,
                    info,
                    cover,
                    location,
                    categories,
                    boothReviewList,
                    boothId
            );
        }).collect(Collectors.toList());
    }
}
