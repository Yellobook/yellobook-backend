package com.yellobook.domain.inventory.controller;

import com.yellobook.common.annotation.TeamMember;
import com.yellobook.common.validation.annotation.ExistInventory;
import com.yellobook.common.validation.annotation.ExistProduct;
import com.yellobook.common.vo.TeamMemberVO;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/inventories")
@Tag(name = " \uD83D\uDCE6 재고", description = "Inventory API")
@RequiredArgsConstructor
@Validated
@Slf4j
public class InventoryController {

    private final InventoryCommandService inventoryCommandService;
    private final InventoryQueryService inventoryQueryService;

    @Operation(summary = "전체 재고 현황 글 조회")
    @GetMapping
    public ResponseEntity<SuccessResponse<GetTotalInventoryResponse>> getTotalInventory(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @TeamMember TeamMemberVO teamMember
            ){
        var result = inventoryQueryService.getTotalInventory(page, size, teamMember);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "일별 재고 현황 상세 조회")
    @GetMapping("/{inventoryId}")
    public ResponseEntity<SuccessResponse<GetProductsResponse>> getProductsByInventory(
            @ExistInventory @PathVariable("inventoryId") Long inventoryId,
            @TeamMember TeamMemberVO teamMember
    ){
        GetProductsResponse response = inventoryQueryService.getProductsByInventory(inventoryId, teamMember);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "재고 검색")
    @GetMapping("/{inventoryId}/search")
    public ResponseEntity<SuccessResponse<GetProductsResponse>> getProductByKeywordAndInventory(
            @ExistInventory @PathVariable("inventoryId") Long inventoryId,
            @RequestParam("keyword") String keyword,
            @TeamMember TeamMemberVO teamMember
    ){
        GetProductsResponse response = inventoryQueryService.getProductByKeywordAndInventory(inventoryId, keyword, teamMember);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "제품 추가")
    @PostMapping("/{inventoryId}")
    public ResponseEntity<SuccessResponse<AddProductResponse>> addProduct(
            @ExistInventory @PathVariable("inventoryId") Long inventoryId,
            @RequestBody AddProductRequest requestDTO,
            @TeamMember TeamMemberVO teamMember
    ){
        AddProductResponse response = inventoryCommandService.addProduct(inventoryId, requestDTO, teamMember);
        return ResponseFactory.success(response);
    }

    @Operation(summary = "제품 수량 수정")
    @PutMapping("/products/{productId}")
    public ResponseEntity<Void> modifyProductAmount(
            @ExistProduct @PathVariable("productId") Long productId,
            @RequestBody ModifyProductAmountRequest requestDTO,
            @TeamMember TeamMemberVO teamMember
    ){
        inventoryCommandService.modifyProductAmount(productId, requestDTO, teamMember);
        return ResponseFactory.noContent();
    }

    @Operation(summary = "제품 삭제")
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @ExistProduct @PathVariable("productId") Long productId,
            @TeamMember TeamMemberVO teamMember
    ){
        inventoryCommandService.deleteProduct(productId, teamMember);
        return ResponseFactory.noContent();
    }
}
