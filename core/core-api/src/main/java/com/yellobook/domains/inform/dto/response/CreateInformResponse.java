package com.yellobook.domains.inform.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CreateInformResponse(
        Long informId,
        LocalDateTime createdAt
) {
}
