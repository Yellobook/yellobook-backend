package com.yellobook.domain.inventory.controller;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.inventory.dto.AddProductRequest;
import com.yellobook.domain.inventory.dto.GetProductsResponse;
import com.yellobook.domain.inventory.dto.GetTotalInventoryResponse;
import com.yellobook.domain.inventory.dto.ModifyProductAmountRequest;
import com.yellobook.domain.inventory.service.InventoryCommandService;
import com.yellobook.domain.inventory.service.InventoryQueryService;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/inventories")
@Tag(name = " \uD83D\uDCE6 재고", description = "Inventory API")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryCommandService inventoryCommandService;
    private final InventoryQueryService inventoryQueryService;

    @Operation(summary = "전체 재고 현황 글 조회")
    @GetMapping("")
    public ResponseEntity<SuccessResponse<GetTotalInventoryResponse>> getTotalInventory(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @AuthenticationPrincipal CustomOAuth2User user
    ){
        inventoryQueryService.getTotalInventory(page, size, user);
        return null;
    }

    @Operation(summary = "특정 일자 재고 현황 상세 조회")
    @GetMapping("/{inventoryId}")
    public ResponseEntity<SuccessResponse<GetProductsResponse>> getProductsByInventory(
            @PathVariable("inventoryId") Long inventoryId,
            @AuthenticationPrincipal CustomOAuth2User user
    ){
        inventoryQueryService.getProductsByInventory(inventoryId);
        return null;
    }

    @Operation(summary = "[관리자] 재고 검색")
    @GetMapping("/{inventoryId}/search")
    public ResponseEntity<SuccessResponse<GetProductsResponse>> getProductByKeywordAndInventory(
            @PathVariable("inventoryId") Long inventoryId,
            @RequestParam("keyword") String keyword,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        inventoryQueryService.getProductByKeywordAndInventory(inventoryId, keyword, oAuth2User);
        return null;
    }

    @Operation(summary = "[관리자] 제품 추가")
    @PostMapping("/{inventoryId}")
    public ResponseEntity<SuccessResponse<String>> addProduct(
            @PathVariable("inventoryId") Long inventoryId,
            @RequestBody AddProductRequest requestDTO,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        inventoryCommandService.addProduct(inventoryId, requestDTO, oAuth2User);
        return null;
    }

    @Operation(summary = "[관리자] 특정 제품 수량 수정")
    @PutMapping("/products/{productId}")
    public ResponseEntity<SuccessResponse<String>> modifyProductAmount(
            @PathVariable("productId") Long productId,
            @RequestBody ModifyProductAmountRequest requestDTO,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        inventoryCommandService.modifyProductAmount(productId, requestDTO, oAuth2User);
        return null;
    }

    @Operation(summary = "[관리자] 특정 제품 삭제")
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<SuccessResponse<String>> deleteProduct(
            @PathVariable("productId") Long productId,
            @AuthenticationPrincipal CustomOAuth2User oAuth2User
    ){
        inventoryCommandService.deleteProduct(productId, oAuth2User);
        return null;
    }
}
