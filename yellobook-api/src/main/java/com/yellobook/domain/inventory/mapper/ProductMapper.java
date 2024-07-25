package com.yellobook.domain.inventory.mapper;

import com.yellobook.domain.inventory.dto.GetProductsResponse;
import com.yellobook.domains.inventory.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface ProductMapper {
    List<GetProductsResponse.ProductInfo> toProductInfo(List<ProductDTO> productDTOList);
}
