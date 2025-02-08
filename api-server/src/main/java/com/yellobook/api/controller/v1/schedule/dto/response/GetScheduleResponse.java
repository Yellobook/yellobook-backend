package com.yellobook.api.controller.schedule.dto.response;

import com.yellobook.core.domain.schedule.Schedule;
import com.yellobook.core.domain.schedule.ScheduleComment;
import com.yellobook.core.domain.schedule.ScheduleMention;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record GetScheduleResponse(
        @Schema(description = "가지고 오는 글의 작성자")
        String author,
        @Schema(description = "가지고 오는 글의 제목")
        String title,
        @Schema(description = "가지고 오는 글에 있는 메모")
        String memo,
        @Schema(description = "함께 하는 멤버")
        List<MentionItem> mentions,
        @Schema(description = "조회수")
        int view,
        @Schema(description = "댓글")
        List<CommentItem> comments,
        @Schema(description = "날짜")
        LocalDate date
) {
    public record MentionItem(
            @Schema(description = "언급된 사용자 ID")
            Long memberId,
            @Schema(description = "언급된 사용자 닉네임")
            String memberNickname
    ) {
    }

    public record CommentItem(
            @Schema(description = "작성한 댓글의 고유 ID")
            Long id,
            @Schema(description = "댓글을 작성한 멤버의 ID")
            String commenterNickname,
            @Schema(description = "작성한 댓글의 내용")
            String content,
            @Schema(description = "작성한 댓글의 생성 시간")
            LocalDateTime createdAt
    ) {
    }

    public static GetScheduleResponse from(Schedule schedule, List<ScheduleMention> mentions,
                                           List<ScheduleComment> comments) {
        return new GetScheduleResponse(
                schedule.author()
                        .profileInfo()
                        .nickname(),
                schedule.title(),
                schedule.content(),
                mentions.stream()
                        .map(mention -> new MentionItem(mention.memberId(), mention.memberName()))
                        .toList(),
                schedule.view(),
                comments.stream()
                        .map(comment -> new CommentItem(comment.commentId(), comment.commenter()
                                .profileInfo()
                                .nickname(),
                                comment.content(), comment.createTime()))
                        .toList(),
                schedule.scheduledDate()
        );
    }
}
