package com.yellobook.core.domain.schedule;

import com.yellobook.core.domain.member.Member;
import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class ScheduleCommentAccessManager {
    private final ScheduleCommentRepository scheduleCommentRepository;

    public ScheduleCommentAccessManager(ScheduleCommentRepository scheduleCommentRepository) {
        this.scheduleCommentRepository = scheduleCommentRepository;
    }

    public void isCommentAuthor(Long commentId, Member member) {
        if (!scheduleCommentRepository.findByCommentId(commentId)
                .commenter()
                .equals(member)) {
            throw new CoreException(CoreErrorType.SCHEDULE_COMMENT_AUTHOR_NOT_MATCH);
        }
    }
}
