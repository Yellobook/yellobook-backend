package com.yellobook.domains.order.dto.response;

import com.yellobook.common.enums.MemberTeamRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record GetOrderCommentsResponse(
        @Schema(description = "댓글 전체")
        List<CommentInfo> comments
) {

    public record CommentInfo(
            @Schema(description = "댓글 Id", example = "1")
            Long commentId,
            @Schema(description = "해당 팀에서 역할", example = "관리자")
            String role,
            @Schema(description = "댓글 내용", example = "주문 수량 20개로 변경 가능할까요?")
            String content
    ){

        @Builder
        public static CommentInfo of(Long commentId, MemberTeamRole role, String content){
            return new CommentInfo(commentId, role.getDescription(), content);
        }
    }

}
