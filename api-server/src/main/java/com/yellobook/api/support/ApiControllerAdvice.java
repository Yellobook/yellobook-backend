package com.yellobook.api.support;

import com.yellobook.api.support.auth.error.AuthException;
import com.yellobook.api.support.error.ApiErrorType;
import com.yellobook.api.support.error.ApiException;
import com.yellobook.api.support.error.ValidationError;
import com.yellobook.api.support.response.ApiResponse;
import com.yellobook.core.error.CoreErrorKind;
import com.yellobook.core.error.CoreException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = CoreException.class)
    public ResponseEntity<Object> handleCoreException(CoreException e) {
        CoreErrorKind errorKind = e.getErrorType()
                .getKind();
        HttpStatus status = switch (errorKind) {
            case BAD_REQUEST -> HttpStatus.BAD_REQUEST;
            case CONFLICT -> HttpStatus.CONFLICT;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
        };

        switch (e.getErrorType()
                .getErrorLevel()) {
            case ERROR -> log.error("CoreException : {}", e.getMessage(), e);
            case WARN -> log.warn("CoreException : {}", e.getMessage(), e);
            case INFO -> log.info("CoreException : {}", e.getMessage(), e);
        }
        return new ResponseEntity<>(ApiResponse.error(e.getErrorType(), e.getData()), status);
    }

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException e) {
        switch (e.getErrorType()
                .getLogLevel()) {
            case ERROR -> log.error("ApiException : {}", e.getMessage(), e);
            case WARN -> log.warn("ApiException : {}", e.getMessage(), e);
            case INFO -> log.info("ApiException : {}", e.getMessage(), e);
        }
        return new ResponseEntity<>(ApiResponse.error(e.getErrorType(), e.getData()), e.getErrorType()
                .getStatus());
    }

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

    /**
     * MethodArgumentNotValidException을 처리한다.
     *
     * @param e       발생한 MethodArgumentNotValidException
     * @param headers HTTP 헤더
     * @param status  HTTP 상태 코드
     * @param request 웹 요청
     * @return BAD_REQUEST 에러코드와 유효성 검사 오류가 포함된 ResponseEntity
     * @Valid로 검증된 객체의 유효성 검사가 실패할 때 발생한다.
     * <p>
     * e) @Valid가 붙은 객체가 컨트롤러 메서드의 파라미터로 전달되었을 때, 해당 객체의 필수 필드가 누락되거나 잘못된 형식의 데이터가 전달되면 발생한다.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        ApiErrorType errorType = ApiErrorType.INVALID_REQUEST;
        List<ValidationError> data = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        log.info("Validation Exception : {}", data, e);
        return new ResponseEntity<>(ApiResponse.error(errorType, data), errorType.getStatus());
    }

    /**
     * ConstraintViolationException을 처리한다.
     *
     * @param e 발생한 ConstraintViolationException
     * @return 에러 코드와 유효성 검사 오류가 포함된 ResponseEntity
     * @PathVariable이나 @RequestParam 등의 검증이 실패할 때 발생한다.
     * <p>
     * e) @Min(1)로 설정된 파라미터가 0이거나 음수일 때 등
     * @Min, @Max, @NotNull 등의 제약 조건이 위배되었을 때
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        ApiErrorType errorType = ApiErrorType.INVALID_REQUEST;
        List<ValidationError> data = e.getConstraintViolations()
                .stream()
                .map(violation -> new ValidationError(violation.getPropertyPath()
                        .toString(), violation.getMessage()))
                .collect(Collectors.toList());
        log.info("Validation Exception : {}", data, e);
        return new ResponseEntity<>(ApiResponse.error(errorType, data), errorType.getStatus());
    }

    /**
     * MethodArgumentTypeMismatchException을 처리한다. 요청 파라미터의 타입이 컨트롤러 메서드의 파라미터 타입과 일치하지 않을 때 발생한다.
     * <p>
     * e) @PathVariable Long id가 있는데 요청 URL에서 /api/items/abc (abc는 Long으로 변환될 수 없음) 처럼 요청한 경우, 즉, 기대하는 타입과 다른 타입이 전달된 경우
     * 발생한다.
     *
     * @param e 발생한 MethodArgumentTypeMismatchException
     * @return BAD_REQUEST 에러 코드와 타입 불일치 오류가 포함된 ResponseEntity
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        ApiErrorType errorType = ApiErrorType.INVALID_PARAMETER_TYPE;
        var data = Map.of(
                "argumentType", e.getName(),
                "requiredType", e.getParameter()
                        .getParameterType()
        );
        log.warn("Method ArgumentTypeMismatchException : {}", data.toString(), e);
        return new ResponseEntity<>(ApiResponse.error(errorType, data), errorType.getStatus());
    }

    /**
     * MissingServletRequestParameterException을 처리한다. 필수 요청 파라미터가 누락된 경우 발생한다.
     * <p>
     * e) @RequestParam(required = true)로 설정된 파라미터가 요청에서 누락된 경우 발생한다.
     *
     * @param e       발생한 MissingServletRequestParameterException
     * @param headers HTTP 헤더
     * @param status  HTTP 상태 코드
     * @param request 웹 요청
     * @return BAD_REQUEST 에러코드와 누락된 파라미터 오류가 포함된 ResponseEntity
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException e,
                                                                          HttpHeaders headers, HttpStatusCode status,
                                                                          WebRequest request) {
        ApiErrorType errorType = ApiErrorType.MISSING_PARAMETER;
        log.warn("Missing Request Parameter Exception: {}", e.getParameterName(), e);
        var data = Map.of(
                "parameterName", e.getParameterName(),
                "parameterType", e.getParameterType()
        );
        return new ResponseEntity<>(ApiResponse.error(errorType, data), errorType.getStatus());
    }

    /**
     * NoHandlerFoundException을 처리한다. 요청된 URL에 대한 핸들러가 없는 경우 발생한다.
     * <p>
     * e) 존재하지 않는 경로로 요청이 들어오는 등, 잘못된 URL로 요청이 들어왔을 때 발생한다.
     *
     * @param e       발생한 NoHandlerFoundException
     * @param headers HTTP 헤더
     * @param status  HTTP 상태 코드
     * @param request 웹 요청
     * @return NOT_FOUND 에러코드가 포함된 ResponseEntity
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpHeaders headers,
                                                                   HttpStatusCode status, WebRequest request) {
        ApiErrorType errorType = ApiErrorType.RESOURCE_NOT_FOUND;
        log.warn("handleNoHandlerFoundException: {}", e.getRequestURL(), e);
        var data = Map.of(
                "requestURL", e.getRequestURL()
        );
        return new ResponseEntity<>(ApiResponse.error(errorType, data), errorType.getStatus());
    }

    /**
     * 모든 예외를 처리한다. 위에서 처리되지 않은 예외가 발생할 때 처리한다.
     * <p>
     * 예시: NullPointerException 등 예기치 않은 예외가 발생할 때 처리한다.
     *
     * @param e       발생한 예외
     * @param request 웹 요청
     * @return INTERNAL_SERVER_ERROR 에러코드가 포함된 ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception e, WebRequest request) {
        ApiErrorType errorType = ApiErrorType.INTERNAL_SERVER_ERROR;
        log.error("Unhandled Exception: {}", e.getMessage(), e);
        return new ResponseEntity<>(ApiResponse.error(errorType), errorType.getStatus());
    }
}
