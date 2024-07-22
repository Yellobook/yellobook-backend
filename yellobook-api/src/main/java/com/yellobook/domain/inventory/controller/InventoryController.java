package com.yellobook.domain.inventory.controller;

import com.yellobook.domain.inventory.dto.AddProductRequest;
import com.yellobook.domain.inventory.dto.GetProductsResponse;
import com.yellobook.domain.inventory.dto.GetTotalInventoryResponse;
import com.yellobook.domain.inventory.dto.ModifyProductAmountRequest;
import com.yellobook.domain.inventory.service.InventoryCommandService;
import com.yellobook.domain.inventory.service.InventoryQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory/{teamId}")
@Tag(name = " \uD83D\uDCE6 재고현황", description = "Inventory & Product API")
@RequiredArgsConstructor
public class InventoryController {

    private InventoryCommandService inventoryCommandService;
    private InventoryQueryService inventoryQueryService;

    @Operation(summary = "전체 재고 현황 글 조회")
    @GetMapping("")
    public ResponseEntity<GetTotalInventoryResponse> getTotalInventory(
            @PathVariable("teamId") Long teamId,
            @RequestParam("page") Long page,
            @RequestParam("size") Long size
    ){
        inventoryQueryService.getTotalInventory(page, size, teamId);
        return null;
    }

    @Operation(summary = "특정 일자 재고 현황 조회")
    @GetMapping("/{inventoryId}")
    public ResponseEntity<GetProductsResponse> getProductsByInventory(
            @PathVariable("teamId") Long teamId,
            @PathVariable("inventoryId") Long inventoryId
    ){
        inventoryQueryService.getProductsByInventory(teamId, inventoryId);
        return null;
    }

    @Operation(summary = "[관리자] 재고 검색")
    @GetMapping("/admin/{inventoryId}/search?keyword={keyword}")
    public ResponseEntity<GetProductsResponse> getProductByKeywordAndInventory(
            @PathVariable("teamId") Long teamId,
            @PathVariable("inventoryId") Long inventoryId,
            @PathVariable("keyword") String keyword
    ){
        inventoryQueryService.getProductByKeywordAndInventory(teamId, inventoryId, keyword);
        return null;
    }

    @Operation(summary = "[관리자] 재고 수량 수정")
    @PutMapping("/admin/{productId}")
    public ResponseEntity<?> modifyProductAmount(
            @PathVariable("teamId") Long teamId,
            @PathVariable("productId") Long productId,
            @RequestBody ModifyProductAmountRequest requestDTO
    ){
        inventoryCommandService.modifyProductAmount(teamId, productId, requestDTO);
        return null;
    }

    @Operation(summary = "[관리자] 제품 추가")
    @PostMapping("/admin/{inventoryId}")
    public ResponseEntity<?> addProduct(
            @PathVariable("teamId") Long teamId,
            @PathVariable("inventoryId") Long inventoryId,
            @RequestBody AddProductRequest requestDTO
    ){
        inventoryCommandService.addProduct(teamId, inventoryId, requestDTO);
        return null;
    }

    @Operation(summary = "[관리자] 제품 삭제")
    @DeleteMapping("/admin/{productId}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable("teamId") Long teamId,
            @PathVariable("productId") Long productId
    ){
        inventoryCommandService.deleteProduct(teamId, productId);
        return null;
    }
}
