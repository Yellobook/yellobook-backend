package com.yellobook.api.support.error;

public record ValidationError(
        String field,
        String message
) {
}
