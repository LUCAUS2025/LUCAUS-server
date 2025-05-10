package com.likelion13.lucaus_api.dto.response.booth;

import com.likelion13.lucaus_api.domain.entity.booth.BoothReview;
import com.likelion13.lucaus_api.enums.BoothCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class BoothDetailResponseDto {
    private Integer dayBoothNum; // 날짜별 부스 번호

    private String name; // 부스이름

    private String owner; // 부스 운영 주체

    private String info; // 부스 소개글

    private String cover; // 부스 커버 이미지

    private String location; // 위치

    private List<String> categories; // 부스카테고리

    private List<Map<String, Integer>> boothReview; // 부스리뷰

    private Long boothId;// 부스 고유 아이디

    //private Integer detailLocation; // 세부위치 번호

    private List<Integer> opDateList; // 운영일자 리스트

    //private Integer opTimeStart; // 운영시작시간 - 안줌

    //private Integer opTimeEnd; // 운영마감시간 - 안줌
}
