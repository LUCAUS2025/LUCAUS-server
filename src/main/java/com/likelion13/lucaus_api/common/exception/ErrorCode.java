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
    INVALID_EMPTY(HttpStatus.BAD_REQUEST, "EMPTY400", "카테고리,날짜,페이지,사이즈는 필수값 입니다.");

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
