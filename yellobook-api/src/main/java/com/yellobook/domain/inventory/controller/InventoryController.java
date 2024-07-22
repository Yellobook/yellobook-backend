package com.yellobook.domain.inventory.controller;

import com.yellobook.domain.inventory.dto.GetTotalInventoryResponse;
import com.yellobook.domain.inventory.service.InventoryCommandService;
import com.yellobook.domain.inventory.service.InventoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
