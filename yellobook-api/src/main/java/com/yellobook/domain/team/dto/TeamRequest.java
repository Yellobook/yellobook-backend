package com.yellobook.domain.team.dto;

import com.yellobook.enums.MemberTeamRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TeamRequest {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateTeamRequestDTO {
        @Schema(description = "회사/매장명", example ="나이키")
        private String name;
        @Schema(description = "회사/매장 전화번호", example ="012345678")
        private String phone;
        @Schema(description = "회사/매장 주소", example ="서울특별시 강남구")
        private String address;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InviteTeamRequestDTO {
        @Schema(description = "피초대자 권한", example = "VIEWER")
        private MemberTeamRole role;
    }
}
