package com.yellobook.api.controller.v1.team.dto.request;

import com.yellobook.core.domain.team.dto.CreateTeamCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTeamRequest(
        @NotBlank(message = "회사/매장명은 필수 입력 사항입니다.")
        @Schema(description = "회사/매장명", example = "나이키")
        String name,
        @NotBlank(message = "팀 스페이스 설명글을 필수 입력 사항입니다.")
        @Schema(description = "팀 스페이스 정보", example = "00이네 딸기농장의 팀 스페이스 입니다.")
        String description,
        @NotBlank(message = "회사/매장 전화번호는 필수 입력 사항입니다.")
        @Schema(description = "회사/매장 전화번호", example = "012345678")
        String phoneNumber,
        @NotBlank(message = "회사/매장 주소는 필수 입력 사항입니다.")
        @Schema(description = "회사/매장 주소", example = "서울특별시 강남구")
        String address,
        @NotNull(message = "팀 스페이스의 공개 여부는 필수 입력 사항입니다.")
        @Schema(description = "공개/비공개", example = "true/false")
        Boolean searchable
) {
    public CreateTeamCommand toCommand() {
        return new CreateTeamCommand(name, description, phoneNumber, address, searchable);
    }
}
