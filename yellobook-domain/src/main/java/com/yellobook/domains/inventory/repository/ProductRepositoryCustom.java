package com.yellobook.domains.inventory.repository;

import com.yellobook.domains.inventory.dto.ProductDTO;

import java.util.List;

public interface ProductRepositoryCustom {
    List<ProductDTO> getProducts(Long inventoryId);
    List<ProductDTO> getProducts(Long inventoryId, String keyword);
}
