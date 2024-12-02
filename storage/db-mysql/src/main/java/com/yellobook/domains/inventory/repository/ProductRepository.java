package com.yellobook.domains.inventory.repository;

import com.yellobook.domains.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    void deleteById(Long productId);

    boolean existsByInventoryIdAndSku(Long inventoryId, Integer sku);

}
