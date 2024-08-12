package com.yellobook.domains.order.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GetSubProductNameResponse(
        List<SubProductInfo> subProducts
) {
    @Builder
    public record SubProductInfo(
            Long productId,
            String subProductName
    ){
    }

}
