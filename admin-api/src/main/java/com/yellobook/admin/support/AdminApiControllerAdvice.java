package com.yellobook.admin.support;

import com.yellobook.admin.support.error.AdminException;
import com.yellobook.admin.support.response.AdminResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.yellobook.admin")
public class AdminApiControllerAdvice {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = AdminException.class)
    public ResponseEntity<Object> handleApiException(AdminException e) {
        switch (e.getErrorType()
                .getLogLevel()) {
            case ERROR -> log.error("AdminException : {}", e.getMessage(), e);
            case WARN -> log.warn("AdminException : {}", e.getMessage(), e);
            case INFO -> log.info("AdminException : {}", e.getMessage(), e);
        }
        return new ResponseEntity<>(AdminResponse.error(e.getErrorType(), e.getData()), e.getErrorType()
                .getStatus());
    }
}
