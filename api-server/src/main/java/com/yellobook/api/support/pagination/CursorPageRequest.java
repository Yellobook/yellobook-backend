package com.yellobook.api.support.pagination;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "커서 기반 페이지네이션 요청")
public class CursorPageRequest {
    public static final int DEFAULT_PAGE_SIZE = 15;

    @Min(value = 1, message = "커서 ID는 1 이상이어야 합니다.")
    @Parameter(description = "커서 ID (null이면 첫 페이지)", example = "100")
    private final Long cursorId;

    @Min(value = 1, message = "요청 데이터 개수는 1개 이상이어야 합니다.")
    @Parameter(description = "가져올 데이터 개수", example = "10")
    private final int size;

    public CursorPageRequest(Long cursorId, Integer size) {
        this.cursorId = cursorId;
        this.size = (size == null || size <= 0) ? DEFAULT_PAGE_SIZE : size;
    }

    public Long getCursorId() {
        return cursorId;
    }

    public int getSize() {
        return size;
    }

    public boolean hasCursor() {
        return cursorId != null;
    }
}
