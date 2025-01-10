package com.yellobook.core.domain.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductWriter {
    private final ProductRepository productRepository;

    @Autowired
    public ProductWriter(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void updateAmount(int amount, Long productId) {
        productRepository.updateAmount(amount, productId);
    }
}
