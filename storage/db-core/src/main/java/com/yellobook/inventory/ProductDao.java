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

    @Override
    public void updateAmount(int amount, Long productId) {
        productJpaRepository.updateAmount(amount, productId);
    }

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

    // getProducts 와 똑같은 역할을 함 (반환 타입만 다름)
//    @Override
//    public List<QueryProductName> getProductsName(Long inventoryId, String productName) {
//        QProduct product = QProduct.product;
//
//        return queryFactory.select(Projections.constructor(QueryProductName.class,
//                        product.name
//                ))
//                .distinct()
//                .from(product)
//                .where(product.inventory.id.eq(inventoryId), product.name.contains(productName.trim()))
//                .orderBy(product.name.asc())
//                .fetch();
//    }

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
