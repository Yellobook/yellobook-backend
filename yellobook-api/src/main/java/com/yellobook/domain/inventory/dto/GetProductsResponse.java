package com.yellobook.domain.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "다수의 제품 정보 조회 - 응답 DTO")
@Getter
public class GetProductsResponse {
    @Schema(description = "제품")
    private List<ProductInfo> products;

    @Builder
    private GetProductsResponse(List<ProductInfo> products){
        this.products = products;
    }

    @Getter
    public static class ProductInfo{
        @Schema(description = "제품 id")
        private Long productId;
        @Schema(description = "제품명")
        private String name;
        @Schema(description = "하위 제품")
        private String subProduct;
        @Schema(description = "품번")
        private Integer sku;
        @Schema(description = "구매가")
        private Integer purchasePrice;
        @Schema(description = "판매가")
        private Integer salePrice;
        @Schema(description = "재고")
        private Integer amount;

        @Builder
        private ProductInfo(Long productId, String name, String subProduct, Integer sku, Integer purchasePrice, Integer salePrice, Integer amount){
            this.productId = productId;
            this.name = name;
            this.subProduct = subProduct;
            this.sku = sku;
            this.purchasePrice = purchasePrice;
            this.salePrice = salePrice;
            this.amount = amount;
        }
    }
}
