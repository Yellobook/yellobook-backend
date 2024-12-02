package com.yellobook.domains.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(description = "전체 재고 현황 글 조회 - 응답 DTO")
@Builder
public record GetTotalInventoryResponse(
        @Schema(description = "페이지 번호 (1번부터 시작)")
        Integer page,
        @Schema(description = "한 페이지에 보여질 게시글 갯수", defaultValue = "5")
        Integer size,
        @Schema(description = "재고 현황 게시글")
        List<InventoryInfo> inventories
) {

    @Builder
    public record InventoryInfo(
            @Schema(description = "재고 현황 id")
            Long inventoryId,
            @Schema(description = "재고 현황 제목")
            String title,
            @Schema(description = "작성일자")
            String createdAt,
            @Schema(description = "마지막 업데이트 일시")
            String updatedAt,
            @Schema(description = "조회수")
            Integer view
    ) {
    }

}
