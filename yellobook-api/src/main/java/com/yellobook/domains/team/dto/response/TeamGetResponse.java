package com.yellobook.domains.team.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record TeamGetResponse(
        @Schema(description = "가져오는 팀의 고유 id",example = "123")
        Long teamId,
        @Schema(description = "가져오는 팀의 이름", example ="나이키")
        String name,
        @Schema(description = "가져오는 팀의 전화번호", example ="012345678")
        String phoneNumber,
        @Schema(description = "가져오는 팀의 주소", example ="서울특별시 강남구")
        String address
) {
}
