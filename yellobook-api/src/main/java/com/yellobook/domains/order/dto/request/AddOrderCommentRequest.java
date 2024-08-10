package com.yellobook.domains.order.dto.request;

import lombok.Builder;

@Builder
public record AddOrderCommentRequest(
        String content
) { }