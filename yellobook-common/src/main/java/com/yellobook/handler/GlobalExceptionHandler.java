package com.yellobook.handler;

import com.yellobook.code.CommonErrorCode;
import com.yellobook.code.ErrorCode;
import com.yellobook.exception.CustomException;
import com.yellobook.response.ResponseFactory;
import com.yellobook.response.ValidationError;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * CustomException을 처리한다.
     *
     * @param exception 발생한 CustomException
     * @return 에러 코드와 메시지가 포함된 ResponseEntity
     */
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.error("CustomException: code={}, message={}", errorCode.getCode(), errorCode.getMessage());
        return ResponseFactory.failure(errorCode);
    }

    /**
     * MethodArgumentNotValidException을 처리한다.
     * @Valid로 검증된 객체의 유효성 검사가 실패할 때 발생한다.
     *
     * ex) @Valid가 붙은 객체가 컨트롤러 메서드의 파라미터로 전달되었을 때,
     * 해당 객체의 필수 필드가 누락되거나 잘못된 형식의 데이터가 전달되면 발생한다.
     *
     * @param ex 발생한 MethodArgumentNotValidException
     * @param headers HTTP 헤더
     * @param status HTTP 상태 코드
     * @param request 웹 요청
     * @return BAD_REQUEST 에러코드와 유효성 검사 오류가 포함된 ResponseEntity
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorCode errorCode = CommonErrorCode.BAD_REQUEST;

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ValidationError> errors = fieldErrors.stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        log.error("Validation error: {}", errors);
        return ResponseFactory.failure(errorCode, errors);
    }

    /**
     * ConstraintViolationException을 처리한다.
     * @PathVariable이나 @RequestParam 등의 검증이 실패할 때 발생한다.
     *
     * ex) @Min(1)로 설정된 파라미터가 0이거나 음수일 때 등
     * @Min, @Max, @NotNull 등의 제약 조건이 위배되었을 때
     *
     * @param ex 발생한 ConstraintViolationException
     * @return 에러 코드와 유효성 검사 오류가 포함된 ResponseEntity
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        ErrorCode errorCode = CommonErrorCode.BAD_REQUEST;

        List<ValidationError> errors = ex.getConstraintViolations().stream()
                .map(violation -> new ValidationError(violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.toList());

        log.error("Constraint violation error: {}", errors);
        return ResponseFactory.failure(errorCode, errors);
    }

    /**
     * MethodArgumentTypeMismatchException을 처리한다.
     * 요청 파라미터의 타입이 컨트롤러 메서드의 파라미터 타입과 일치하지 않을 때 발생한다.
     *
     * ex) @PathVariable Long id가 있는데 요청 URL에서 /api/items/abc (abc는 Long으로 변환될 수 없음) 처럼 요청한 경우,
     * 즉, 기대하는 타입과 다른 타입이 전달된 경우 발생한다.
     *
     *
     * @param ex 발생한 MethodArgumentTypeMismatchException
     * @return BAD_REQUEST 에러 코드와 타입 불일치 오류가 포함된 ResponseEntity
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ErrorCode errorCode = CommonErrorCode.BAD_REQUEST;
        String errorMessage = String.format("Parameter '%s' should be of type '%s'", ex.getName(), ex.getRequiredType().getSimpleName());
        log.error("Method argument type mismatch: {}", errorMessage);
        return ResponseFactory.failure(errorCode, List.of(new ValidationError(ex.getName(), errorMessage)));
    }

    /**
     * MissingServletRequestParameterException을 처리한다.
     * 필수 요청 파라미터가 누락된 경우 발생한다.
     *
     * ex) @RequestParam(required = true)로 설정된 파라미터가 요청에서 누락된 경우 발생한다.
     *
     * @param ex 발생한 MissingServletRequestParameterException
     * @param headers HTTP 헤더
     * @param status HTTP 상태 코드
     * @param request 웹 요청
     * @return BAD_REQUEST 에러코드와 누락된 파라미터 오류가 포함된 ResponseEntity
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorCode errorCode = CommonErrorCode.BAD_REQUEST;
        log.error("Missing request parameter: {}", ex.getParameterName());
        return ResponseFactory.failure(errorCode);
    }

    /**
     * NoHandlerFoundException을 처리한다.
     * 요청된 URL에 대한 핸들러가 없는 경우 발생한다.
     *
     * ex) 존재하지 않는 경로로 요청이 들어오는 등,
     * 잘못된 URL로 요청이 들어왔을 때 발생한다.
     *
     * @param ex 발생한 NoHandlerFoundException
     * @param headers HTTP 헤더
     * @param status HTTP 상태 코드
     * @param request 웹 요청
     * @return NOT_FOUND 에러코드가 포함된 ResponseEntity
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorCode errorCode = CommonErrorCode.NOT_FOUND;
        log.error("No handler found for URL: {}", ex.getRequestURL());
        return ResponseFactory.failure(errorCode);
    }

    /**
     * 모든 예외를 처리한다.
     * 위에서 처리되지 않은 예외가 발생할 때 처리한다.
     *
     * 예시: NullPointerException 등 예기치 않은 예외가 발생할 때 처리한다.
     *
     * @param ex 발생한 예외
     * @param request 웹 요청
     * @return INTERNAL_SERVER_ERROR 에러코드가 포함된 ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        log.error("Unhandled exception: ", ex);
        return ResponseFactory.failure(errorCode);
    }
}
