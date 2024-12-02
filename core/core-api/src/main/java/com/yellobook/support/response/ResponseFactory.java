package com.yellobook.support.response;

import com.yellobook.support.error.code.ErrorCode;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseFactory {

    private static final String SUCCESS_MESSAGE = "요청이 성공적으로 처리되었습니다.";
    private static final String CREATED_MESSAGE = "리소스가 성공적으로 생성되었습니다.";

    private ResponseFactory() {
        throw new UnsupportedOperationException("팩토리 유틸리티 메소드는 인스턴스화 할 수 없습니다.");
    }

    public static <T> ResponseEntity<SuccessResponse<T>> success(T data) {
        return buildSuccessResponse(SUCCESS_MESSAGE, HttpStatus.OK, data);
    }

    public static <T> ResponseEntity<SuccessResponse<T>> created(T data) {
        return buildSuccessResponse(CREATED_MESSAGE, HttpStatus.CREATED, data);
    }

    public static ResponseEntity<Void> noContent() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private static <T> ResponseEntity<SuccessResponse<T>> buildSuccessResponse(String message, HttpStatus statusCode,
                                                                               T data) {
        SuccessResponse<T> successResponse = SuccessResponse.<T>builder()
                .isSuccess(true)
                .message(message)
                .data(data)
                .build();
        return new ResponseEntity<>(successResponse, statusCode);
    }

    // ErrorResponse 생성 메서드
    public static ResponseEntity<Object> failure(ErrorCode errorCode) {
        return buildErrorResponse(errorCode, null);
    }

    public static ResponseEntity<Object> failure(ErrorCode errorCode, List<ValidationError> errors) {
        return buildErrorResponse(errorCode, errors);
    }

    private static ResponseEntity<Object> buildErrorResponse(ErrorCode errorCode, List<ValidationError> errors) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .errors(errors)
                .build();
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }
}

