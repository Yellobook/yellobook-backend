package com.yellobook.core.domain.schedule;

import com.yellobook.core.domain.member.Member;
import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class ScheduleAccessManager {
    private final ScheduleReader scheduleReader;
    private final ScheduleMentionProcessor scheduleMentionProcessor;

    public ScheduleAccessManager(ScheduleReader informReader, ScheduleMentionProcessor informMentionProcessor) {
        this.scheduleReader = informReader;
        this.scheduleMentionProcessor = informMentionProcessor;
    }

    public void isAuthorOrMentioned(Long scheduleId, Long memberId) {
        Schedule schedule = scheduleReader.read(scheduleId);
        if (!schedule.author()
                .memberId()
                .equals(memberId) && !scheduleMentionProcessor.isMentioned(scheduleId, memberId)) {
            throw new CoreException(CoreErrorType.INFORM_ACCESS_NOT_ALLOWED);
        }
    }

    public void isAuthor(Long scheduleId, Member author) {
        Schedule schedule = scheduleReader.read(scheduleId);
        if (!schedule.author()
                .equals(author)) {
            throw new CoreException(CoreErrorType.INFORM_AUTHOR_NOT_MATCH);
        }
    }
}
