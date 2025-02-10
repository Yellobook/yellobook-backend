package com.yellobook.api.controller.v1.team.dto.response;

import com.yellobook.core.domain.team.Participant;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record GetParticipantsResponse(
        List<ParticipantResponse> participants
) {
    public record ParticipantResponse(
            @Schema(description = "사용자의 고유 id", example = "123")
            Long memberId,
            @Schema(description = "사용자의 닉네임", example = "하하호호")
            String nickname,
            @Schema(description = "사용자의 팀에서의 역할 (판매자, 주문자, 뷰어 중 하나)", example = "판매자")
            String role
    ) {
    }

    public static GetParticipantsResponse from(List<Participant> participants) {
        return new GetParticipantsResponse(participants.stream()
                .map(p -> new ParticipantResponse(p.memberId(), p.nickname(), p.role()
                        .getDisplayName()))
                .toList());
    }
}
