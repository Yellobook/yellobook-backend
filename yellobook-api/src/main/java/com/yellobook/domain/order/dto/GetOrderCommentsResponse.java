package com.yellobook.domain.order.dto;

import com.yellobook.common.enums.MemberTeamRole;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class GetOrderCommentsResponse {
    private List<CommentInfo> comments;

    @Builder
    private GetOrderCommentsResponse(List<CommentInfo> comments){
        this.comments = comments;
    }

    @Getter
    public static class CommentInfo{
        private Long commentId;
        private String role;
        private String content;

        @Builder
        private CommentInfo(Long commentId, MemberTeamRole role, String content){
            this.commentId = commentId;
            this.role = role.getDescription();
            this.content = content;
        }
    }

}
