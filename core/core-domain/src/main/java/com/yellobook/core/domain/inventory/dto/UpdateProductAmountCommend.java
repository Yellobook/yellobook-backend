package com.yellobook.core.domain.inventory.dto;

public record UpdateProductAmountCommend(
        Long productId,
        int amount
) {
}
