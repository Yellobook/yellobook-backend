package com.yellobook.core.domain.inform;

public record NewInformComment(
        Long commenterId,
        Long informId,
        String content
) {
}
