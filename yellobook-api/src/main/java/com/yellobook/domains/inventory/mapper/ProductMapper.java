package com.yellobook.domains.inventory.mapper;

import com.yellobook.domains.inventory.dto.request.AddProductRequest;
import com.yellobook.domains.inventory.dto.response.GetProductsResponse;
import com.yellobook.domains.inventory.dto.query.QueryProduct;
import com.yellobook.domains.inventory.entity.Inventory;
import com.yellobook.domains.inventory.entity.Product;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface ProductMapper {
    List<GetProductsResponse.ProductInfo> toProductInfo(List<QueryProduct> queryProductList);
    Product toProduct(AddProductRequest addProductRequest, Inventory inventory);
}
