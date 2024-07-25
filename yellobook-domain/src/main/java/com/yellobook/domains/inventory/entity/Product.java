package com.yellobook.domains.inventory.entity;

import com.yellobook.domains.common.entity.BaseEntity;
import com.yellobook.domains.inventory.entity.Inventory;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "products",
        uniqueConstraints = {
            // 재고현황별 품번은 고유해야 한다.
            @UniqueConstraint(name = "uc_inventory_sku", columnNames = {"inventory_id", "sku"})
        }
)
public class Product extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 재고 현황
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    /**
     * 상품명
     */
    @Column(nullable = false, length = 30)
    private String name;

    /**
     * 하위 품목
     */
    @Column(length = 30)
    private String subProduct;

    /**
     * 상품 일련번호
     */
    @Column(nullable = false)
    private Integer sku;

    /**
     * 구매가격
     */
    @Column(nullable = false)
    private Integer purchasePrice;

    /**
     * 판매가격
     */
    @Column(nullable = false)
    private Integer salePrice;

    /**
     * 상품재고수량
     */
    @Column(nullable = false)
    private Integer amount;

    public void modifyAmount(Integer amount){
        this.amount = amount;
    }
}
