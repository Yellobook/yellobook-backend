package com.yellobook.core.domain.schedule;

import org.springframework.stereotype.Component;

@Component
public class ScheduleCommentWriter {
    private final ScheduleCommentRepository scheduleCommentRepository;

    public ScheduleCommentWriter(ScheduleCommentRepository scheduleCommentRepository) {
        this.scheduleCommentRepository = scheduleCommentRepository;
    }

    public Long add(NewScheduleComment commend) {
        return scheduleCommentRepository.save(commend);
    }
}
