package com.yellobook.core.domains.order.repository;

import com.yellobook.core.domains.inventory.entity.Product;
import com.yellobook.core.domains.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    boolean existsByProduct(Product product);

}
