package com.yellobook.domains.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InventoryDTO {
    private final Long inventoryId;
    private final String title;
    private final String createdAt;
    private final String updatedAt;
    private final Integer view;

//    @QueryProjection
//    public InventoryDTO(Long inventoryId, String title, String createdAt, String updatedAt, Integer view){
//        this.inventoryId = inventoryId;
//        this.title = title;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//        this.view = view;
//    }
}
