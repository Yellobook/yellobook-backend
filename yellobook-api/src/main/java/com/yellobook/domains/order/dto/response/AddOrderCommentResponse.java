package com.yellobook.domains.order.dto.response;

import lombok.Builder;

@Builder
public record AddOrderCommentResponse(
    Long commentId
) { }
