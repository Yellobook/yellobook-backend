package com.yellobook.api.controller.v1.schedule.dto.response;

import com.yellobook.core.domain.schedule.ScheduleMemberSearchItem;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record GetMemberMentionResponse(
        @Schema(description = "검색 키워드를 포함한 멤버 모음")
        List<searchItem> searchItems
) {
    public record searchItem(
            @Schema(description = "검색 키워드를 포함한 멤버 id")
            Long memberId,
            @Schema(description = "검색 키워드를 포함한 멤버 닉네임")
            String nickname,
            @Schema(description = "검색 키워드를 포함한 멤버의 프로필 이미지")
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
