package com.yellobook.core.domain.schedule;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ScheduleCommentService {
    private final ScheduleCommentReader scheduleCommentReader;
    private final ScheduleCommentWriter scheduleCommentWriter;
    private final ScheduleAccessManager scheduleAccessManager;

    public ScheduleCommentService(ScheduleCommentReader scheduleCommentReader,
                                  ScheduleCommentWriter scheduleCommentWriter,
                                  ScheduleAccessManager scheduleAccessManager) {
        this.scheduleCommentReader = scheduleCommentReader;
        this.scheduleCommentWriter = scheduleCommentWriter;
        this.scheduleAccessManager = scheduleAccessManager;
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
