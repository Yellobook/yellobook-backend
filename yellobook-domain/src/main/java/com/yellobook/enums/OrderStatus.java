package com.yellobook.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING_CONFIRM,
    PENDING_MODIFY,
    CONFIRMED,
}
