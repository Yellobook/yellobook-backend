package com.yellobook.core.domain.schedule;

import com.yellobook.core.domain.member.MemberReader;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMentionProcessor {
    private final ScheduleRepository scheduleRepository;
    private final MemberReader memberReader;

    public ScheduleMentionProcessor(ScheduleRepository scheduleRepository, MemberReader memberReader) {
        this.scheduleRepository = scheduleRepository;
        this.memberReader = memberReader;
    }

    public Boolean isMentioned(Long scheduleId, Long memberId) {
        return scheduleRepository.isMentioned(scheduleId, memberId);
    }

    public void mention(Long scheduleId, List<Long> mentionIds) {
        scheduleRepository.mention(scheduleId, mentionIds);
    }
}
