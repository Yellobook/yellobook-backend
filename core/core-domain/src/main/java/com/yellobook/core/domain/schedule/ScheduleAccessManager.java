package com.yellobook.core.domain.schedule;

import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.team.TeamJoinLeaveManager;
import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class ScheduleAccessManager {
    private final ScheduleReader scheduleReader;
    private final ScheduleMentionProcessor scheduleMentionProcessor;
    private final TeamJoinLeaveManager teamJoinLeaveManager;

    public ScheduleAccessManager(ScheduleReader scheduleReader, ScheduleMentionProcessor scheduleMentionProcessor,
                                 TeamJoinLeaveManager teamJoinLeaveManager) {
        this.scheduleReader = scheduleReader;
        this.scheduleMentionProcessor = scheduleMentionProcessor;
        this.teamJoinLeaveManager = teamJoinLeaveManager;
    }

    public void isAuthorOrMentioned(Schedule schedule, Member requester) {
        if (!schedule.author()
                .equals(requester) && !scheduleMentionProcessor.isMentioned(schedule.scheduleId(), requester)) {
            throw new CoreException(CoreErrorType.SCHEDULE_ACCESS_NOT_ALLOWED);
        }
    }

    public void isAuthor(Long scheduleId, Member requester) {
        Schedule schedule = scheduleReader.read(scheduleId);
        if (!schedule.author()
                .equals(requester)) {
            throw new CoreException(CoreErrorType.SCHEDULE_AUTHOR_NOT_MATCH);
        }
    }

    public void isAbleToCreate(Long teamId, Member requester) {
        teamJoinLeaveManager.hasJoinedTeam(teamId, requester.memberId());
    }
}
