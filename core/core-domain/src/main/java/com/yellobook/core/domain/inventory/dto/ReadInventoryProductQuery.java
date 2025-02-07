package com.yellobook.core.domain.inventory.dto;

import com.yellobook.core.domain.inventory.Inventory;
import com.yellobook.core.domain.inventory.Product;
import java.util.List;

public record ReadInventoryProductQuery(
        Inventory inventory,
        List<Product> products
) {
}
