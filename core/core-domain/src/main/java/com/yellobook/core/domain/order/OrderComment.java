package com.yellobook.core.domain.order;

import java.time.LocalDateTime;

public record OrderComment(
        Long orderCommentId,
        OrderCommenter commenter,
        String comment,
        LocalDateTime writtenAt
) {
}
