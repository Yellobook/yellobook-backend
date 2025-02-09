package com.yellobook.api.security;

import com.yellobook.api.security.error.AuthException;
import com.yellobook.api.support.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.yellobook.api.security")
public class AuthControllerAdvice {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = AuthException.class)
    public ResponseEntity<Object> handleAuthException(AuthException e) {
        switch (e.getErrorType()
                .getLogLevel()) {
            case ERROR -> log.error("AuthException : {}", e.getMessage(), e);
            case WARN -> log.warn("AuthException : {}", e.getMessage(), e);
            case INFO -> log.info("AuthException : {}", e.getMessage(), e);
        }
        return new ResponseEntity<>(ApiResponse.error(e.getErrorType()), e.getErrorType()
                .getStatus());
    }
}
