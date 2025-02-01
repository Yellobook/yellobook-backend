package com.yellobook.api.controller.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record GetProductsNameResponse(
        @Schema(description = "검색에 매칭되는 제품 이름, 해당 값을 하위 제품 검색 시 전달해주세요.")
        List<String> names
) {
}
