package com.yellobook.domains.product.repository;

import com.yellobook.domains.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    void deleteById(Long productId);

}
