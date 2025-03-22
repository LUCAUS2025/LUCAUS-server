package com.likelion13.lucaus_api.common.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ErrorReasonDTO {

    private HttpStatus httpStatus;

    private final Boolean isSuccess;
    private final String code;
    private final String message;
}
