package com.yellobook.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    void deleteById(Long productId);

    boolean existsByInventoryIdAndSku(Long inventoryId, Integer sku);
}
