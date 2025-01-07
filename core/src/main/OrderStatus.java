package com.yellobook;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    /**
     * 주문 확인 대기중 (DEFAULT)
     */
    PENDING_CONFIRM,
    /**
     * 주문 정정 대기중
     */
    PENDING_MODIFY,
    /**
     * 주문 확인 완료
     */
    CONFIRMED,
}
