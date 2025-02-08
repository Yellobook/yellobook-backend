package com.yellobook.core.domain.schedule;

import com.yellobook.core.domain.member.Member;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMentionProcessor {
    private final ScheduleRepository scheduleRepository;

    public ScheduleMentionProcessor(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Boolean isMentioned(Long scheduleId, Member member) {
        return scheduleRepository.isMentioned(scheduleId, member);
    }

    public void mention(Long scheduleId, List<Long> mentionIds) {
        scheduleRepository.mention(scheduleId, mentionIds);
    }

    public List<ScheduleMention> getMentions(Long scheduleId) {
        return scheduleRepository.getMentionsByScheduleId(scheduleId);
    }
}
