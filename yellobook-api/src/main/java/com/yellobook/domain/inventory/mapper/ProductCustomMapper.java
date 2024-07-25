package com.yellobook.domain.inventory.mapper;

import com.yellobook.domain.inventory.dto.AddProductRequest;
import com.yellobook.domains.inventory.entity.Product;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class ProductCustomMapper {
    @Named("toProduct")
    public Product toProduct(AddProductRequest addProductRequest) {
        if ( addProductRequest == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.name( addProductRequest.getName() );
        product.subProduct( addProductRequest.getSubProduct() );
        product.sku( addProductRequest.getSku() );
        product.purchasePrice( addProductRequest.getPurchasePrice() );
        product.salePrice( addProductRequest.getSalePrice() );
        product.amount( addProductRequest.getAmount() );

        return product.build();
    }
}
