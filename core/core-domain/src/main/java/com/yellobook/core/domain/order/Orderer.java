package com.yellobook.core.domain.order;

public record Orderer(
        Long ordererId,
        String nickname
) {
}
