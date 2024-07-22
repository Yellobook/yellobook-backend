package com.yellobook.domains.inventory.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InventoryDto {
    private Long inventoryId;
    private String title;
    private String createdAt;
    private String updatedAt;
    private Integer view;
}
