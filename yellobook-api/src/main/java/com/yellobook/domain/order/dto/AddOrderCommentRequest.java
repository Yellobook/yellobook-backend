package com.yellobook.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AddOrderCommentRequest {
    private String content;

    @Builder
    private AddOrderCommentRequest(String content){
        this.content = content;
    }
}