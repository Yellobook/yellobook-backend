package com.yellobook.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AddOrderCommentResponse {
    private Long commentId;

    @Builder
    private AddOrderCommentResponse(Long commentId){
        this.commentId = commentId;
    }
}
