package com.yellobook.domains.product.entity;

import com.yellobook.domains.common.entity.BaseEntity;
import com.yellobook.domains.teamspace.entity.Teamspace;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "products",
        uniqueConstraints = {
            @UniqueConstraint(name = "uc_teamspaceId_sku", columnNames = {"teamspace_id", "sku"})
        }
)
public class Product extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamspace_id", nullable = false)
    private Teamspace teamspace;

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
