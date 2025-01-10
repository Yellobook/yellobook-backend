package com.yellobook.core.domain.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductReader {
    private final ProductRepository productRepository;

    @Autowired
    public ProductReader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


}
