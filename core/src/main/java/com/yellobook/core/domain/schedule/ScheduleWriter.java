package com.yellobook.core.domain.schedule;

import org.springframework.stereotype.Component;

@Component
public class ScheduleWriter {
    private final ScheduleRepository scheduleRepository;

    public ScheduleWriter(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Long create(NewSchedule newSchedule) {
        return scheduleRepository.save(newSchedule);
    }
}
