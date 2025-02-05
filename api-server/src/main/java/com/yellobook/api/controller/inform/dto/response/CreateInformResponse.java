package com.yellobook.api.controller.inform.dto.response;

import java.time.LocalDateTime;

public record CreateInformResponse(
        Long informId,
        LocalDateTime createdAt
) {
}
