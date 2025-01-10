package com.yellobook.core.domain.inventory;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository {
    void updateAmount(int amount, Long productId);
}
