package com.yellobook.api.controller.v1.schedule.dto.response;

import com.yellobook.core.domain.schedule.ScheduleMemberSearchItem;
import java.util.List;

public record GetMemberMentionResponse(
        List<searchItem> searchItems
) {
    public record searchItem(
            Long memberId,
            String nickname,
            String profileImage
    ) {
    }

    public static GetMemberMentionResponse from(List<ScheduleMemberSearchItem> items) {
        return new GetMemberMentionResponse(
                items.stream()
                        .map(item -> new searchItem(item.memberId(), item.nickname(), item.profileImage()))
                        .toList()
        );
    }
}
