package com.yellobook.domains.product.repository;

import com.yellobook.domains.product.ProductDto;

import java.util.List;

public interface ProductRepositoryCustom {
    List<ProductDto> getProducts(Long inventoryId);
    List<ProductDto> getProducts(Long inventoryId, String keyword);
}
