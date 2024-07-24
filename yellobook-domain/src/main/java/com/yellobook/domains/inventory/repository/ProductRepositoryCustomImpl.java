package com.yellobook.domains.inventory.repository;

import com.yellobook.domains.inventory.dto.ProductDTO;

import java.util.List;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom{
    @Override
    public List<ProductDTO> getProducts(Long inventoryId) {
        return null;
    }

    @Override
    public List<ProductDTO> getProducts(Long inventoryId, String keyword) {
        return null;
    }
}
