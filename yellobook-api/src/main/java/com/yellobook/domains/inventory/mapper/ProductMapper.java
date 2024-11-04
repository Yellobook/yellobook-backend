package com.yellobook.domains.inventory.mapper;

import com.yellobook.domains.inventory.dto.cond.ExcelProductCond;
import com.yellobook.domains.inventory.dto.query.QueryProduct;
import com.yellobook.domains.inventory.dto.query.QueryProductName;
import com.yellobook.domains.inventory.dto.query.QuerySubProduct;
import com.yellobook.domains.inventory.dto.request.AddProductRequest;
import com.yellobook.domains.inventory.dto.response.AddProductResponse;
import com.yellobook.domains.inventory.dto.response.GetProductsNameResponse;
import com.yellobook.domains.inventory.dto.response.GetProductsResponse;
import com.yellobook.domains.inventory.dto.response.GetProductsResponse.ProductInfo;
import com.yellobook.domains.inventory.dto.response.GetSubProductNameResponse;
import com.yellobook.domains.inventory.dto.response.GetSubProductNameResponse.SubProductInfo;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.inventory.entity.Product;
import java.util.List;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(AddProductRequest addProductRequest, Inventory inventory);

    Product toProduct(ExcelProductCond productCond, Inventory inventory);

    AddProductResponse toAddProductResponse(Long productId);

    List<ProductInfo> toProductInfo(List<QueryProduct> queryProductList);

    default GetProductsResponse toGetProductsResponse(List<QueryProduct> products) {
        return GetProductsResponse.builder()
                .products(toProductInfo(products))
                .build();
    }

    default GetProductsNameResponse toGetProductsNameResponse(List<QueryProductName> productNames) {
        return GetProductsNameResponse.builder()
                .names(productNames.stream()
                        .map(QueryProductName::name)
                        .toList())
                .build();
    }

    default GetSubProductNameResponse toGetSubProductNameResponse(List<QuerySubProduct> subProducts) {
        return GetSubProductNameResponse.builder()
                .subProducts(toSubProductInfo(subProducts))
                .build();
    }

    List<SubProductInfo> toSubProductInfo(List<QuerySubProduct> subProducts);
}
