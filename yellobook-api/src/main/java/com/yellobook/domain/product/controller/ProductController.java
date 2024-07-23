package com.yellobook.domain.product.controller;

import com.yellobook.domain.inventory.dto.ModifyProductAmountRequest;
import com.yellobook.domain.product.service.ProductCommandService;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products/{teamId}")
@Tag(name = " \uD83E\uDDE9 상품", description = "Product API")
@RequiredArgsConstructor
public class ProductController {
    private final ProductCommandService productCommandService;

    @Operation(summary = "[관리자] 제품 수량 수정")
    @PutMapping("/{productId}")
    public ResponseEntity<SuccessResponse<String>> modifyProductAmount(
            @PathVariable("teamId") Long teamId,
            @PathVariable("productId") Long productId,
            @RequestBody ModifyProductAmountRequest requestDTO
    ){
        productCommandService.modifyProductAmount(teamId, productId, requestDTO);
        return null;
    }

    @Operation(summary = "[관리자] 제품 삭제")
    @DeleteMapping("/{productId}")
    public ResponseEntity<SuccessResponse<String>> deleteProduct(
            @PathVariable("teamId") Long teamId,
            @PathVariable("productId") Long productId
    ){
        productCommandService.deleteProduct(teamId, productId);
        return null;
    }
}
