package com.yellobook.domains.product.entity;

import com.yellobook.domains.common.entity.BaseEntity;
import com.yellobook.domains.inventory.entity.Inventory;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "products",
        uniqueConstraints = {
            @UniqueConstraint(name = "uc_inventoryId_sku", columnNames = {"inventory_id", "sku"})
        }
)
public class Product extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(length = 30)
    private String subProduct;

    @Column(nullable = false)
    private Integer sku;

    @Column(nullable = false)
    private Integer purchasePrice;

    @Column(nullable = false)
    private Integer salePrice;

    @Column(nullable = false)
    private Integer amount;
}
