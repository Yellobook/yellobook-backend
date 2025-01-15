package com.yellobook.core.domain.order;

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
     * 주문 정정 요청 완료 후 대기중
     */
    PENDING_MODIFY,
    /**
     * 주문 확인 완료
     */
    CONFIRMED,
}