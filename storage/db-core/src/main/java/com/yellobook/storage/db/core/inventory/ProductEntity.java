package com.yellobook.storage.db.core.inventory;

import com.yellobook.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "products",
        uniqueConstraints = {
                // 재고현황별 품번은 고유해야 한다.
                @UniqueConstraint(name = "uc_inventory_sku", columnNames = {"inventory_id", "sku"})
        }
)
public class ProductEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private InventoryEntity inventory;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(length = 30, nullable = false)
    private String subProduct;

    @Column(nullable = false)
    private Integer sku;

    @Column(nullable = false)
    private Integer purchasePrice;

    @Column(nullable = false)
    private Integer salePrice;

    @Column(nullable = false)
    private Integer amount;

    protected ProductEntity() {
    }

    public ProductEntity(String name, InventoryEntity inventory, String subProduct, Integer sku, Integer purchasePrice,
                         Integer salePrice, Integer amount) {
        this.name = name;
        this.inventory = inventory;
        this.subProduct = subProduct;
        this.sku = sku;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.amount = amount;
    }

    public void modifyAmount(Integer amount) {
        this.amount = amount;
    }

    public void reduceAmount(Integer orderAmount) {
        this.amount -= orderAmount;
    }

    public InventoryEntity getInventory() {
        return inventory;
    }

    public String getName() {
        return name;
    }

    public String getSubProduct() {
        return subProduct;
    }

    public Integer getSku() {
        return sku;
    }

    public Integer getPurchasePrice() {
        return purchasePrice;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public Integer getAmount() {
        return amount;
    }
}
