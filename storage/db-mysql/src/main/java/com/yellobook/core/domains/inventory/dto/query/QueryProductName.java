package com.yellobook.core.domains.inventory.dto.query;

import lombok.Builder;

@Builder
public record QueryProductName(
        String name
) {
}
