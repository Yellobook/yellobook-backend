package com.yellobook.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddOrderCommentRequest {
    private String content;
}