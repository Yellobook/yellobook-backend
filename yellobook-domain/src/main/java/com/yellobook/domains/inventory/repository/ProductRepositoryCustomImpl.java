package com.yellobook.domains.inventory.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.domains.inventory.dto.query.QueryProduct;
import com.yellobook.domains.inventory.dto.query.QueryProductName;
import com.yellobook.domains.inventory.dto.query.QuerySubProduct;
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
                .where(product.inventory.id.eq(inventoryId), product.name.contains(keyword.trim()))
                .orderBy(product.name.asc())
                .fetch();
    }

    @Override
    public List<QueryProductName> getProductsName(Long inventoryId, String productName) {
        QProduct product = QProduct.product;

        return queryFactory.select(Projections.constructor(QueryProductName.class,
                        product.name
                )).distinct()
                .from(product)
                .where(product.inventory.id.eq(inventoryId), product.name.contains(productName.trim()))
                .orderBy(product.name.asc())
                .fetch();
    }

    @Override
    public List<QuerySubProduct> getSubProducts(Long inventoryId, String productName) {
        QProduct product = QProduct.product;

        return queryFactory.select(Projections.constructor(QuerySubProduct.class,
                        product.id.as("productId"),
                        product.subProduct.as("subProductName")))
                .from(product)
                .where(product.inventory.id.eq(inventoryId), product.name.eq(productName.trim()))
                .orderBy(product.subProduct.asc())
                .fetch();
    }

}
