package com.yellobook.domains.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryDTO {
    private Long inventoryId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer view;

//    @QueryProjection
//    public InventoryDTO(Long inventoryId, String title, String createdAt, String updatedAt, Integer view){
//        this.inventoryId = inventoryId;
//        this.title = title;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//        this.view = view;
//    }
}
