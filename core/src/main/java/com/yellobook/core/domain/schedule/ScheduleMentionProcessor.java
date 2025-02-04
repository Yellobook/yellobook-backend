package com.yellobook.core.domain.schedule;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMentionProcessor {
    private final ScheduleRepository scheduleRepository;

    public ScheduleMentionProcessor(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Boolean isMentioned(Long scheduleId, Long memberId) {
        return scheduleRepository.isMentioned(scheduleId, memberId);
    }

    public void mention(Long scheduleId, List<Long> mentionIds) {
        scheduleRepository.mention(scheduleId, mentionIds);
    }
}
