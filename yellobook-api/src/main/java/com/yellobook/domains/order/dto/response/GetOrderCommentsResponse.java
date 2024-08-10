package com.yellobook.domains.order.dto.response;

import com.yellobook.common.enums.MemberTeamRole;
import lombok.Builder;

import java.util.List;

@Builder
public record GetOrderCommentsResponse(
    List<CommentInfo> comments
) {

    public record CommentInfo(
            Long commentId,
            String role,
            String content
    ){

        @Builder
        public static CommentInfo of(Long commentId, MemberTeamRole role, String content){
            return new CommentInfo(commentId, role.getDescription(), content);
        }
    }

}
