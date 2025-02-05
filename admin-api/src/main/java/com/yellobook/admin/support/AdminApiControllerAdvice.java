package com.yellobook.admin.support;

import com.yellobook.admin.support.error.AdminErrorType;
import com.yellobook.admin.support.error.AdminException;
import com.yellobook.admin.support.response.AdminResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice(basePackageClasses = AdminApiControllerAdvice.class)
public class AdminApiControllerAdvice {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = AdminException.class)
    public ResponseEntity<Object> handleApiException(AdminException e) {
        switch (e.getErrorType()
                .getLogLevel()) {
            case ERROR -> log.error("ApiException : {}", e.getMessage(), e);
            case WARN -> log.warn("ApiException : {}", e.getMessage(), e);
            case INFO -> log.info("ApiException : {}", e.getMessage(), e);
        }
        return new ResponseEntity<>(AdminResponse.error(e.getErrorType(), e.getData()), e.getErrorType()
                .getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception e, WebRequest request) {
        AdminErrorType errorType = AdminErrorType.INTERNAL_SERVER_ERROR;
        log.error("Unhandled Exception: {}", e.getMessage(), e);
        return new ResponseEntity<>(AdminResponse.error(errorType), errorType.getStatus());
    }
}
