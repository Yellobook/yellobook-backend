package com.yellobook.domains.inventory.dto.query;

import lombok.Builder;

@Builder
public record QuerySubProduct(
        Long productId,
        String subProductName
) {
}
