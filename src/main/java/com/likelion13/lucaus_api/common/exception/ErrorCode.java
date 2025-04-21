package com.likelion13.lucaus_api.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseErrorCode {

    // 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "CATEGORY400", "잘못된 카테고리 입니다. 유효한 카테고리 (DAILY_NECESSITIES, ELECTRONICS, CLOTHING, WALLET_CARD, OTHERS, TOTAL)"),
    INVALID_DATE(HttpStatus.BAD_REQUEST, "DATE400", "잘못된 날짜 형식입니다. (형식: YYYY-MM-DD)"),
    INVALID_PAGE_SIZE(HttpStatus.BAD_REQUEST, "PAGE400", "페이지와 사이즈는 1보다 커야합니다."),
    INVALID_EMPTY(HttpStatus.BAD_REQUEST, "EMPTY400", "카테고리,날짜,페이지,사이즈는 필수값 입니다."),
    NOTICE_NOT_FOUND(HttpStatus.BAD_REQUEST,"NOTICE404", "해당 공지사항을 찾을 수 없습니다."),


    // 운영일자 어긴 경우
    INVALID_OP_DATE(HttpStatus.BAD_REQUEST, "OPDATE400", "opDate는 19~23 사이의 하나의 Integer 입니다."),

    // 리뷰 관련 시간 어긴 경우
    INVALID_REVIEW_TIME(HttpStatus.BAD_REQUEST, "REVIEW4001", "운영시간 중에만 리뷰를 보낼 수 있습니다."),

    // 리뷰 태그 잘못된 경우
    INVALID_REVIEW_TAG(HttpStatus.BAD_REQUEST, "REVIEW4002", "리뷰 태그 잘못되었습니다."),

    // 부스관련
    NOT_FOUND_BOOTH(HttpStatus.NOT_FOUND, "BOOTH404", "조건에 맞는 부스를 찾을 수 없습니다."),

    // 푸드트럭 관련
    NOT_FOUND_FOOD_TRUCK(HttpStatus.NOT_FOUND, "FOOD-TRUCK404", "조건에 맞는 푸드트럭을 찾을 수 없습니다."),

    // 방문자 수 관련
    INVALID_VISITOR_NUM(HttpStatus.BAD_REQUEST, "VISITOR400", "방문자 수는 필수값 입니다.(Integer)"),

    // 회원가입 관련 정책
    INVALID_ID_LENGTH(HttpStatus.BAD_REQUEST, "AUTH4001", "ID는 4자리 이상이어야 합니다."),
    INVALID_PW_LENGTH(HttpStatus.BAD_REQUEST, "AUTH4002", "PW는 4자리 이상이어야 합니다."),
    INVALID_STUDENTNUM_LENGTH(HttpStatus.BAD_REQUEST, "AUTH4003", "학번은 8자리 이상이어야 합니다."),
    INVALID_STUDENTNUM_FORMAT(HttpStatus.BAD_REQUEST, "AUTH4004", "학번은 숫자로만 구성되어야 합니다."),

    // 회원가입 에러
    DUPLICATED_ID(HttpStatus.BAD_REQUEST, "AUTH4005", "중복된 아이디입니다."),

    // 로그인 관련
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "LOGIN404", "존재하지 않는 ID입니다."),

    // pw관련
    WRONG_PW(HttpStatus.BAD_REQUEST, "PW400", "잘못된 PW입니다."),

    // 중복 도장
    DUPLICATED_STAMP(HttpStatus.BAD_REQUEST, "STAMP400", "중복된 도장"),

    // 상품 수령 관련
    NOT_ENOUGH_STAMP(HttpStatus.BAD_REQUEST, "REWARD4001", "도장 개수 불충분"),
    INVALID_DEGREE(HttpStatus.BAD_REQUEST, "REWARD4002", "잘못된 차수"),
    DUPLICATED_REWARD(HttpStatus.BAD_REQUEST, "REWARD4003", "중복 수령");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
