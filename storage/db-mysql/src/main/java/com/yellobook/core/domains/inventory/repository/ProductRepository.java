package com.yellobook.core.domains.inventory.repository;

import com.yellobook.core.domains.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    void deleteById(Long productId);

    boolean existsByInventoryIdAndSku(Long inventoryId, Integer sku);

}
