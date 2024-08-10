package com.yellobook.domains.team.dto;

import com.yellobook.common.enums.MemberTeamRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TeamRequest {
    @Getter
    @AllArgsConstructor
    public static class CreateTeamRequestDTO {
        @Schema(description = "회사/매장명", example ="나이키")
        private String name;
        @Schema(description = "회사/매장 전화번호", example ="012345678")
        private String phoneNumber;
        @Schema(description = "회사/매장 주소", example ="서울특별시 강남구")
        private String address;
        @Schema(description = "팀을 생성한 사람의 권한", example = "ADMIN")
        private MemberTeamRole role;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InvitationCodeRequestDTO {
        @Schema(description = "피초대자 권한", example = "VIEWER")
        private MemberTeamRole role;
    }
}
