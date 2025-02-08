package com.yellobook.core.domain.schedule;

import org.springframework.stereotype.Component;

@Component
public class ScheduleViewProcessor {
    private final ScheduleRepository scheduleRepository;

    public ScheduleViewProcessor(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public void increase(Long scheduleId) {
        scheduleRepository.increaseView(scheduleId);
    }
}
