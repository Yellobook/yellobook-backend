package com.yellobook.inventory;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.core.domain.inventory.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao implements ProductRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final ProductJpaRepository productJpaRepository;

    public ProductDao(JPAQueryFactory jpaQueryFactory, ProductJpaRepository productJpaRepository) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.productJpaRepository = productJpaRepository;
    }

    public List<com.yellobook.domains.inventory.dto.query.QueryProduct> getProducts(Long inventoryId) {
        QProduct product = QProduct.product;

        return queryFactory.select(Projections.constructor(com.yellobook.domains.inventory.dto.query.QueryProduct.class,
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
}
