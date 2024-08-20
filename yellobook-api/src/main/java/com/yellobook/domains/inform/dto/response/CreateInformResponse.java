package com.yellobook.domains.inform.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateInformResponse (
        Long informId,
        LocalDateTime createdAt
) {
}
