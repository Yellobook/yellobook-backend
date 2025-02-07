package com.yellobook.core.domain.inventory.dto;

import java.util.List;

public record ReadInventoryProductIds(
        Long inventoryId,
        List<Long> productIds
) {
}
