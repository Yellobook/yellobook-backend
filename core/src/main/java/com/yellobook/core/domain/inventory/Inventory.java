package com.yellobook.core.domain.inventory;


public record Inventory(
        Long inventoryId,
        Long teamId,
        String title,
        int view
) {
}
