package com.yellobook.order;

import com.yellobook.core.domain.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
    @Query(value = "select case when count(o) > 0 then true else false end "
            + "from OrderEntity o where o.product.id = :productId")
    boolean existsByProductId(@Param("productId") Long productId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update OrderEntity o set o.orderStatus = :orderStatus where o.id = :id")
    void updateOrderStatus(@Param("orderStatus") OrderStatus orderStatus, @Param("id") Long id);
}
