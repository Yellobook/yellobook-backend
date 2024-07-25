package com.yellobook.error.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum InventoryErrorCode implements ErrorCode{
    INVENTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "INVENTORY-001", "해당 재고 현황은 존재하지 않습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "INVENTORY-002", "해당 제품은 존재하지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
