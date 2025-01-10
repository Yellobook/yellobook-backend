package com.yellobook.api.controller.team.dto.request;

import com.yellobook.TeamMemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTeamRequest(
        @NotBlank(message = "회사/매장명은 필수 입력 사항입니다.")
        @Schema(description = "회사/매장명", example = "나이키")
        String name,
        @NotBlank(message = "회사/매장 전화번호는 필수 입력 사항입니다.")
        @Schema(description = "회사/매장 전화번호", example = "012345678")
        String phoneNumber,
        @NotBlank(message = "회사/매장 주소는 필수 입력 사항입니다.")
        @Schema(description = "회사/매장 주소", example = "서울특별시 강남구")
        String address,
        @NotNull(message = "팀 개설자의 권한을 설정하십시오.")
        @Schema(description = "팀을 생성한 사람의 권한", example = "ADMIN")
        TeamMemberRole role
) {
}
