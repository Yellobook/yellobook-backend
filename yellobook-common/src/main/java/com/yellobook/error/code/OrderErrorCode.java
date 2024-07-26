package com.yellobook.error.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OrderErrorCode implements ErrorCode{
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER-001", "해당 주문은 존재하지 않습니다."),
    ORDER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "ORDER-002", "접근할 수 없는 주문입니다."),
    ORDER_CONFIRMED_CANT_MODIFY(HttpStatus.CONFLICT, "ORDER-003", "관리자가 주문 확인한 주문은 주문 정정 요청 불가능합니다."),
    ORDER_PENDING_MODIFY_CANT_CONFIRM(HttpStatus.CONFLICT, "ORDER-004", "관리자가 주문 정정한 주문은 주문 확정 불가능합니다."),
    ORDER_CANT_CANCEL(HttpStatus.CONFLICT, "ORDER-005", "주문 정정 상태가 아닌 주문은 최소가 불가능 합니다."),
    ORDER_CREATION_NOT_ALLOWED(HttpStatus.FORBIDDEN, "ORDER-006", "팀에 관리자가 없으면 주문 등록이 불가능 합니다."),
    ORDER_AMOUNT_EXCEED(HttpStatus.CONFLICT, "ORDER-007", "주문 수량이 제품 수량을 초과할 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
