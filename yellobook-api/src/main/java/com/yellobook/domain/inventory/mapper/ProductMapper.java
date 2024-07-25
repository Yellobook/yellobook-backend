package com.yellobook.domain.inventory.mapper;

import com.yellobook.domain.inventory.dto.AddProductRequest;
import com.yellobook.domain.inventory.dto.GetProductsResponse;
import com.yellobook.domains.inventory.dto.ProductDTO;
import com.yellobook.domains.inventory.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    List<GetProductsResponse.ProductInfo> toProductInfo(List<ProductDTO> productDTOList);
    Product toProduct(AddProductRequest addProductRequest);
}
