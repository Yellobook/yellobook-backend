package com.yellobook.domains.inventory.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.domains.inventory.dto.query.QueryProduct;
import com.yellobook.domains.inventory.entity.QInventory;
import com.yellobook.domains.inventory.entity.QProduct;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom{
    public ProductRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    private final JPAQueryFactory queryFactory;

    @Override
    public List<QueryProduct> getProducts(Long inventoryId) {
        QProduct product = QProduct.product;
        QInventory inventory = QInventory.inventory;

        return queryFactory.select(Projections.constructor(QueryProduct.class,
                    product.id.as("productId"),
                    product.name,
                    product.subProduct,
                    product.sku,
                    product.purchasePrice,
                    product.salePrice,
                    product.amount
                ))
                .from(product)
                .where(product.inventory.id.eq(inventoryId))
                .orderBy(product.name.asc())
                .fetch();
    }

    @Override
    public List<QueryProduct> getProducts(Long inventoryId, String keyword) {
        QProduct product = QProduct.product;
        QInventory inventory = QInventory.inventory;

        return queryFactory.select(Projections.constructor(QueryProduct.class,
                        product.id.as("productId"),
                        product.name,
                        product.subProduct,
                        product.sku,
                        product.purchasePrice,
                        product.salePrice,
                        product.amount
                ))
                .from(product)
                .where(product.inventory.id.eq(inventoryId), product.name.contains(keyword))
                .orderBy(product.name.asc())
                .fetch();
    }
}
