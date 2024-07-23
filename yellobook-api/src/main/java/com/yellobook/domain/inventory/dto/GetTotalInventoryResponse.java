package com.yellobook.domain.inventory.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetTotalInventoryResponse {
    private Long page;
    private Long size;
    private List<InventoryInfo> inventories;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class InventoryInfo{
        private Long inventoryId;
        private String title;
        private String createdAt;
        private String updatedAt;
        private Integer view;
    }
}
