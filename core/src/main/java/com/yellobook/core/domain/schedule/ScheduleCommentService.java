package com.yellobook.core.domain.schedule;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ScheduleCommentService {
    private final ScheduleCommentReader scheduleCommentReader;
    private final ScheduleCommentWriter scheduleCommentWriter;

    public ScheduleCommentService(ScheduleCommentReader scheduleCommentReader,
                                  ScheduleCommentWriter scheduleCommentWriter) {
        this.scheduleCommentReader = scheduleCommentReader;
        this.scheduleCommentWriter = scheduleCommentWriter;
    }

    @Transactional
    public Long add(NewScheduleComment comment) {
        return scheduleCommentWriter.add(comment);
    }

    public List<ScheduleComment> getComments(
            Long scheduleId,
            Long memberId
    ) {
        return scheduleCommentReader.readComments(scheduleId);
    }
}
