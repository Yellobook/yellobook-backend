package com.yellobook.domain.order.dto.request;

import lombok.Builder;

@Builder
public record AddOrderCommentRequest(
        String content
) { }