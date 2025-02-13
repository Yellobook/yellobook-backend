package com.yellobook.api.support.error;

public record ApiValidationError(
        String field,
        String message
) {
}
