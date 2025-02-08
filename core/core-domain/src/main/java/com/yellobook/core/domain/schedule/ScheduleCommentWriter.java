package com.yellobook.core.domain.schedule;

import org.springframework.stereotype.Component;

@Component
public class ScheduleCommentWriter {
    private final ScheduleCommentRepository scheduleCommentRepository;

    public ScheduleCommentWriter(ScheduleCommentRepository scheduleCommentRepository) {
        this.scheduleCommentRepository = scheduleCommentRepository;
    }

    public Long add(NewScheduleComment comment) {
        return scheduleCommentRepository.save(comment);
    }

    public void delete(Long commentId) {
        scheduleCommentRepository.deleteByCommentId(commentId);
    }

    public void deleteByScheduleId(Long scheduleId) {
        scheduleCommentRepository.deleteCommentsByScheduleId(scheduleId);
    }
}
