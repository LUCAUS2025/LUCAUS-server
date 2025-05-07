// OpDateServiceImpl.java
package com.likelion13.lucaus_api.service.booth;

import com.likelion13.lucaus_api.common.exception.ErrorCode;
import com.likelion13.lucaus_api.common.exception.GeneralHandler;
import com.likelion13.lucaus_api.domain.repository.booth.OpDateBoothRepository;
import com.likelion13.lucaus_api.dto.response.booth.BoothListByDateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoothOpDateServiceImpl implements BoothOpDateService {

    private final OpDateBoothRepository opDateBoothRepository;

    public List<BoothListByDateResponseDto> getBoothListByDate(Integer opDate) {

        // 운영일자 잘못된 경우
        if(!(opDate >=19 && opDate <=23)) {
            throw new GeneralHandler(ErrorCode.INVALID_OP_DATE);
        }

        // 부스 정보 조회
        List<Object[]> boothResults = opDateBoothRepository.findBoothListByOpDate(opDate);

        // 조회된 부스 없는 경우
        if(boothResults.isEmpty()) {
            throw new GeneralHandler(ErrorCode.NOT_FOUND_BOOTH);
        }

        // 조회된 부스 ID들을 모아서 리스트로 변환
        List<Long> boothIds = boothResults.stream()
                .map(row -> ((Number) row[1]).longValue())
                .collect(Collectors.toList());

        // 카테고리 정보 조회
        Map<Long, List<String>> categoriesMap = new HashMap<>();
        if (!boothIds.isEmpty()) {
            List<Object[]> categoryResults = opDateBoothRepository.findCategoriesByBoothIds(boothIds);

            for (Object[] row : categoryResults) {
                Long boothId = ((Number) row[0]).longValue();
                String categoriesStr = (String) row[1];
                List<String> categories = List.of(categoriesStr.split(","));
                categoriesMap.put(boothId, categories);
            }
        }

        //완전 추천해요 리뷰 숫자 조회
        Map<Long, Integer> recommendMap = new HashMap<>();
        if (!boothIds.isEmpty()) {
            List<Object[]> recommendResults = opDateBoothRepository.findRecommendByBoothIds(boothIds);

            for (Object[] row : recommendResults) {
                Long boothId = ((Number) row[0]).longValue();
                Integer recommendNum = (Integer) row[1];
                recommendMap.put(boothId, recommendNum);
            }
        }

        // 결과 DTO로 변환
        return boothResults.stream().map(row -> {
            Integer dayBoothNum = (Integer) row[0];
            Long boothId = ((Number) row[1]).longValue();
            String name = (String) row[2];
            String owner = (String) row[3];
            List<String> categories = categoriesMap.getOrDefault(boothId, List.of());
            Integer recommendNums = recommendMap.getOrDefault(boothId, 0);

            return new BoothListByDateResponseDto(dayBoothNum, name, owner, categories, recommendNums);
        }).collect(Collectors.toList());
    }
}