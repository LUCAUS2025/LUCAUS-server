package com.likelion13.lucaus_api.common.exception;

public interface BaseErrorCode {
    public ErrorReasonDTO getReason();
    public ErrorReasonDTO getReasonHttpStatus();
}
