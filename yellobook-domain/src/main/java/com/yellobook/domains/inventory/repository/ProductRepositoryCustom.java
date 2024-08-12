package com.yellobook.domains.inventory.repository;

import com.yellobook.domains.inventory.dto.query.QueryProduct;
import com.yellobook.domains.inventory.dto.query.QuerySubProduct;

import java.util.List;

public interface ProductRepositoryCustom {
    List<QueryProduct> getProducts(Long inventoryId);
    List<QueryProduct> getProducts(Long inventoryId, String keyword);
    List<QuerySubProduct> getSubProducts(Long inventoryId, String name);
}
