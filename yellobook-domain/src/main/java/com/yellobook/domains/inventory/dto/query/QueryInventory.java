package com.yellobook.domains.inventory.dto.query;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record QueryInventory(
        Long inventoryId,
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer view
) {
}
