package com.yellobook.domains.product.repository;

import com.yellobook.domains.product.dto.ProductDTO;

import java.util.List;

public interface ProductRepositoryCustom {
    List<ProductDTO> getProducts(Long inventoryId);
    List<ProductDTO> getProducts(Long inventoryId, String keyword);
}
