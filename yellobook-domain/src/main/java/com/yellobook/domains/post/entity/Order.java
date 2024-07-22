package com.yellobook.domains.post.entity;

import com.yellobook.domains.product.entity.Product;
import com.yellobook.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("ORDER")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends Post{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer orderAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
