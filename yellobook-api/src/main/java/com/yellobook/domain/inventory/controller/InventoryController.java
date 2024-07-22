package com.yellobook.domain.inventory.controller;

import com.yellobook.domain.inventory.dto.GetProductsResponse;
import com.yellobook.domain.inventory.dto.GetTotalInventoryResponse;
import com.yellobook.domain.inventory.service.InventoryCommandService;
import com.yellobook.domain.inventory.service.InventoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private InventoryCommandService inventoryCommandService;
    private InventoryQueryService inventoryQueryService;

    // 전제 재고 현황 글 조회
    @GetMapping("/{teamId}")
    public ResponseEntity<GetTotalInventoryResponse> getTotalInventory(
            @PathVariable("teamId") Long teamId,
            @RequestParam("page") Long page,
            @RequestParam("size") Long size
    ){
        //inventoryQueryService.getTotalInventory(page, size, teamId);
        return null;
    }

    // 특정 일자 재고현황 조회
    @GetMapping("/{teamId}/{inventoryId}")
    public ResponseEntity<GetProductsResponse> getProductByInventory(
            @PathVariable("teamId") Long teamId,
            @PathVariable("inventoryId") Long inventoryId
    ){
        //inventoryQueryService.getProductByInventory(teamId, inventoryId);
        return null;
    }

    // 재고 검색 (관리자)
    @GetMapping("/{teamId}/admin/{inventoryId}/search?keyword={keyword}")
    public ResponseEntity<GetProductsResponse> getProductByKeywordAndInventory(
            @PathVariable("teamId") Long teamId,
            @PathVariable("inventoryId") Long inventoryId,
            @PathVariable("keyword") String keyword
    ){
        //inventoryQueryService.getProductByKeywordAndInventory(teamId, inventoryId, keyword);
        return null;
    }
}
