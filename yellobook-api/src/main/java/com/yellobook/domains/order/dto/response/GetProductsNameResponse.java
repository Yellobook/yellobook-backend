package com.yellobook.domains.order.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GetProductsNameResponse(
        List<String> names
) {
}
