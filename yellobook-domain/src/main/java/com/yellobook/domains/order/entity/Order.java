package com.yellobook.domains.order.entity;

import com.yellobook.domains.common.entity.Post;
import com.yellobook.domains.inventory.entity.Product;
import com.yellobook.enums.OrderStatus;
import com.yellobook.enums.PostType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue(PostType.Values.ORDER)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends Post {
    /**
     * 주문 상품
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * 주문 수량
     */
    @Column(nullable = false)
    private Integer orderAmount;

    /**
     * 상품 주문 상태
     * 주문확정: CONFIRMED
     * 주문확정대기: PENDING_CONFIRM
     * 주문정정대기: PENDING_MODIFY
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
