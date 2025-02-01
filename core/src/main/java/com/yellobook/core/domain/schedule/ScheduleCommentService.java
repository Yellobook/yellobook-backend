package com.yellobook.core.domain.schedule;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ScheduleCommentService {
    private final ScheduleCommentReader scheduleCommentReader;
    private final ScheduleCommentWriter scheduleCommentWriter;
    private final ScheduleAccessManager scheduleAccessManager;

    public ScheduleCommentService(ScheduleCommentReader informCommentReader, ScheduleCommentWriter informCommentWriter,
                                  ScheduleAccessManager informAccessManager) {
        this.scheduleCommentReader = informCommentReader;
        this.scheduleCommentWriter = informCommentWriter;
        this.scheduleAccessManager = informAccessManager;
    }

    @Transactional
    public Long add(NewScheduleComment commend) {
        return scheduleCommentWriter.add(commend);
    }


    public List<ScheduleComment> getComments(
            Long scheduleId,
            Long memberId
    ) {
        return scheduleCommentReader.readComments(scheduleId);
    }
}
