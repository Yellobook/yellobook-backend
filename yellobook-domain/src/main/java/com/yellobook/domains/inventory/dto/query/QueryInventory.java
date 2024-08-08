package com.yellobook.domains.inventory.dto.query;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record QueryInventory(
        Long inventoryId,
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer view
) {
}
