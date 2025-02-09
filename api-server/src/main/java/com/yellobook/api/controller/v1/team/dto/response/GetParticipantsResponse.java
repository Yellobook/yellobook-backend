package com.yellobook.api.controller.v1.team.dto.response;

import com.yellobook.core.domain.team.Participant;
import java.util.List;

public record GetParticipantsResponse(
        List<ParticipantResponse> participants
) {
    public record ParticipantResponse(
            Long memberId,
            String nickname,
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
