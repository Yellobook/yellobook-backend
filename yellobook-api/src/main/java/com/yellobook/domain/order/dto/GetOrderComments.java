package com.yellobook.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetOrderComments {
    private Long commentId;
    private String content;

    @Builder
    public GetOrderComments(Long commentId, String content){
        this.commentId = commentId;
        this.content = content;
    }
}
