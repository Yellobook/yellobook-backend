package com.yellobook.domain.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "전체 재고 현황 글 조회 - 응답 DTO")
@Getter
public class GetTotalInventoryResponse {
    @Schema(description = "페이지 번호 (1번부터 시작)")
    private Integer page;
    @Schema(description = "한 페이지에 보여질 게시글 갯수", defaultValue = "5")
    private Integer size;
    @Schema(description = "재고 현황 게시글")
    private List<InventoryInfo> inventories;

    @Builder
    private GetTotalInventoryResponse(Integer page, Integer size, List<InventoryInfo> inventories){
        this.page = page;
        this.size = size;
        this.inventories = inventories;
    }

    @Getter
    public static class InventoryInfo{
        @Schema(description = "재고 현황 id")
        private Long inventoryId;
        @Schema(description = "재고 현황 제목")
        private String title;
        @Schema(description = "작성일자")
        private String createdAt;
        @Schema(description = "마지막 업데이트 일시")
        private String updatedAt;
        @Schema(description = "조회수")
        private Integer view;

        @Builder
        private InventoryInfo(Long inventoryId, String title, String createdAt, String updatedAt, Integer view){
            this.inventoryId = inventoryId;
            this.title = title;
            this.updatedAt = updatedAt;
            this.createdAt = createdAt;
            this.view = view;
        }
    }

}
