package com.yellobook.core.api.support.error.code;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getHttpStatus();

    String getCode();

    String getMessage();
}