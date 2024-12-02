package com.yellobook.core.api.support.error.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum InventoryErrorCode implements ErrorCode {
    INVENTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "INVENTORY-001", "해당 재고 현황은 존재하지 않습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "INVENTORY-002", "해당 제품은 존재하지 않습니다."),
    SKU_ALREADY_EXISTS(HttpStatus.CONFLICT, "INVENTORY-003", "이미 존재하는 품번입니다."),
    ORDER_RELATED(HttpStatus.CONFLICT, "INVENTORY-004", "주문과 연결되어 있는 제품이라서 삭제가 불가능합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
