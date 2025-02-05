package com.yellobook.domains.inventory.dto.query;

import lombok.Builder;

@Builder
public record QueryProductName(
        String name
) {
}
