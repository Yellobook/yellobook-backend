package com.yellobook.domain.inventory.controller;

import com.yellobook.common.annotation.ExistInventory;
import com.yellobook.common.annotation.ExistProduct;
import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.inventory.dto.*;
import com.yellobook.domain.inventory.service.InventoryCommandService;
import com.yellobook.domain.inventory.service.InventoryQueryService;
import com.yellobook.response.ResponseFactory;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/inventories")
@Tag(name = " \uD83D\uDCE6 재고", description = "Inventory API")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryCommandService inventoryCommandService;
    private final InventoryQueryService inventoryQueryService;

    @Operation(summary = "전체 재고 현황 글 조회")
    @GetMapping("")
    public ResponseEntity<SuccessResponse<GetTotalInventoryResponse>> getTotalInventory(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        log.info("memberId: {}", oAuth2User.getMemberId());
        GetTotalInventoryResponse response = inventoryQueryService.getTotalInventory(page, size, oAuth2User);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "특정 일자 재고 현황 상세 조회")
    @GetMapping("/{inventoryId}")
    public ResponseEntity<SuccessResponse<GetProductsResponse>> getProductsByInventory(
            @ExistInventory @PathVariable("inventoryId") Long inventoryId,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        GetProductsResponse response = inventoryQueryService.getProductsByInventory(inventoryId, oAuth2User);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "[관리자] 재고 검색")
    @GetMapping("/{inventoryId}/search")
    public ResponseEntity<SuccessResponse<GetProductsResponse>> getProductByKeywordAndInventory(
            @ExistInventory @PathVariable("inventoryId") Long inventoryId,
            @RequestParam("keyword") String keyword,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        GetProductsResponse response = inventoryQueryService.getProductByKeywordAndInventory(inventoryId, keyword, oAuth2User);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "[관리자] 제품 추가")
    @PostMapping("/{inventoryId}")
    public ResponseEntity<SuccessResponse<AddProductResponse>> addProduct(
            @ExistInventory @PathVariable("inventoryId") Long inventoryId,
            @RequestBody AddProductRequest requestDTO,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        AddProductResponse response = inventoryCommandService.addProduct(inventoryId, requestDTO, oAuth2User);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "[관리자] 특정 제품 수량 수정")
    @PutMapping("/products/{productId}")
    public ResponseEntity<SuccessResponse<String>> modifyProductAmount(
            @ExistProduct @PathVariable("productId") Long productId,
            @RequestBody ModifyProductAmountRequest requestDTO,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        inventoryCommandService.modifyProductAmount(productId, requestDTO, oAuth2User);
        return ResponseFactory.success("제품의 수량을 성공적으로 수정했습니다");
    }

    @Operation(summary = "[관리자] 특정 제품 삭제")
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<SuccessResponse<String>> deleteProduct(
            @ExistProduct @PathVariable("productId") Long productId,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        inventoryCommandService.deleteProduct(productId, oAuth2User);
        return ResponseFactory.success("제품을 성공적으로 삭제했습니다.");
    }
}
