package com.yellobook.domain.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema(description = "전체 재고 현황 글 조회 - 응답 DTO")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetTotalInventoryResponse {
    @Schema(description = "페이지 번호 (1번부터 시작)")
    private Integer page;
    @Schema(description = "한 페이지에 보여질 게시글 갯수", defaultValue = "5")
    private Integer size;
    @Schema(description = "재고 현황 게시글")
    private List<InventoryInfo> inventories;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    }
}
