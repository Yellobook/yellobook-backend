package com.yellobook.api.support.pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "커서 기반 페이지네이션 응답")
public record CursorPageResponse<T>(
        @Schema(description = "데이터 목록", required = true)
        List<T> data,

        @Schema(description = "다음 페이지를 위한 커서 ID (없으면 마지막 페이지)", example = "1024")
        Long nextCursorId,

        @Schema(description = "다음 페이지가 있는지 여부", example = "true")
        boolean hasNext,

        @Schema(description = "첫 번째 페이지 여부", example = "true")
        boolean isFirstPage,

        @Schema(description = "마지막 페이지 여부", example = "false")
        boolean isLastPage
) {
    public static <T> CursorPageResponse<T> of(List<T> data, Long prevCursorId, Long nextCursorId) {
        boolean isFirstPage = prevCursorId == null;
        boolean isLastPage = nextCursorId == null;
        return new CursorPageResponse<>(data, nextCursorId, nextCursorId != null, isFirstPage, isLastPage);
    }
}
