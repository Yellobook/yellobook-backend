package com.yellobook.core.domain.team;

public record TeamApplyInfo(
        Long applyId,
        Long teamId,
        Long memberId,
        JoinStatus joinStatus
) {
}
