package com.yellobook.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    void deleteById(Long productId);

    boolean existsByInventoryIdAndSku(Long inventoryId, Integer sku);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update ProductEntity p set p.amount = :amount where p.id = :id")
    void updateAmount(@Param("amount") int amount, @Param("id") Long id);
}
