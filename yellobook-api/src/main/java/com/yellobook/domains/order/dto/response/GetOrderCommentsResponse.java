package com.yellobook.domains.order.dto.response;

import com.yellobook.common.enums.MemberTeamRole;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record GetOrderCommentsResponse(
    List<CommentInfo> comments
) {

    public record CommentInfo(
            Long commentId,
            String role,
            String content,
            LocalDateTime createdAt
    ){

        @Builder
        public static CommentInfo of(Long commentId, MemberTeamRole role, String content, LocalDateTime createdAt){
            return new CommentInfo(commentId, role.getDescription(), content, createdAt);
        }
    }

}
