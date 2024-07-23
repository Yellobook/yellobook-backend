package com.yellobook.domain.inventory.controller;

import com.yellobook.domain.inventory.dto.AddProductRequest;
import com.yellobook.domain.inventory.dto.GetProductsResponse;
import com.yellobook.domain.inventory.dto.GetTotalInventoryResponse;
import com.yellobook.domain.inventory.service.InventoryCommandService;
import com.yellobook.domain.inventory.service.InventoryQueryService;
import com.yellobook.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventories/{teamId}")
@Tag(name = " \uD83D\uDCE6 재고현황", description = "Inventory API")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryCommandService inventoryCommandService;
    private final InventoryQueryService inventoryQueryService;

    @Operation(summary = "전체 재고 현황 글 조회")
    @GetMapping("")
    public ResponseEntity<SuccessResponse<GetTotalInventoryResponse>> getTotalInventory(
            @PathVariable("teamId") Long teamId,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ){
        inventoryQueryService.getTotalInventory(page, size, teamId);
        return null;
    }

    @Operation(summary = "특정 일자 재고 현황 상세 조회")
    @GetMapping("/{inventoryId}")
    public ResponseEntity<SuccessResponse<GetProductsResponse>> getProductsByInventory(
            @PathVariable("teamId") Long teamId,
            @PathVariable("inventoryId") Long inventoryId
    ){
        inventoryQueryService.getProductsByInventory(teamId, inventoryId);
        return null;
    }

    @Operation(summary = "[관리자] 재고 검색")
    @GetMapping("/{inventoryId}/search")
    public ResponseEntity<SuccessResponse<GetProductsResponse>> getProductByKeywordAndInventory(
            @PathVariable("teamId") Long teamId,
            @PathVariable("inventoryId") Long inventoryId,
            @RequestParam("keyword") String keyword
    ){
        inventoryQueryService.getProductByKeywordAndInventory(teamId, inventoryId, keyword);
        return null;
    }

    @Operation(summary = "[관리자] 제품 추가")
    @PostMapping("/{inventoryId}")
    public ResponseEntity<SuccessResponse<String>> addProduct(
            @PathVariable("teamId") Long teamId,
            @PathVariable("inventoryId") Long inventoryId,
            @RequestBody AddProductRequest requestDTO
    ){
        inventoryCommandService.addProduct(teamId, inventoryId, requestDTO);
        return null;
    }
}
