package com.yellobook.order;

import com.yellobook.inventory.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long>, OrderRepositoryCustom {
    boolean existsByProduct(ProductEntity product);
}
