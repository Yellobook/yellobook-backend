package com.yellobook.controller.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record GetOrderCommentsResponse(
        @Schema(description = "댓글 전체")
        List<CommentInfo> comments
) {

    @Builder
    public record CommentInfo(
            @Schema(description = "댓글 Id", example = "1")
            Long commentId,
            @Schema(description = "해당 팀에서 역할", example = "관리자")
            String role,
            @Schema(description = "댓글 내용", example = "주문 수량 20개로 변경 가능할까요?")
            String content,
            @Schema(description = "댓글 생성 날짜", example = "2007-12-03T10:15:30")
            LocalDateTime createdAt
    ) {
    }

}
