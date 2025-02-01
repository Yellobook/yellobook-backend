package com.yellobook.core.domain.schedule;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ScheduleCommentReader {
    private final ScheduleCommentRepository scheduleCommentRepository;

    public ScheduleCommentReader(ScheduleCommentRepository scheduleCommentRepository) {
        this.scheduleCommentRepository = scheduleCommentRepository;
    }

    public List<ScheduleComment> readComments(Long informId) {
        return scheduleCommentRepository.findCommentsByInformId(informId);
    }
}
