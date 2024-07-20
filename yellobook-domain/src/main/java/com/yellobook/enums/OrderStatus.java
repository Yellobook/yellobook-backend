package com.yellobook.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING, // 관리자 -> 아무것도 안함
    MODIFY_REQUESTED,  // 관리자 -> 주문 정정 요청
    CONFIRMED,  // 관리자 -> 주문 확인
    CANCELLED;  // 주문자 -> 주문 최소 (Order를 아예 삭제하는 방법도 있음)
}
