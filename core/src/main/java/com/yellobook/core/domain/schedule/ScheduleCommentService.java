package com.yellobook.core.domain.schedule;

import com.yellobook.core.domain.member.Member;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ScheduleCommentService {
    private final ScheduleCommentReader scheduleCommentReader;
    private final ScheduleCommentWriter scheduleCommentWriter;
    private final ScheduleCommentAccessManager scheduleCommentAccessManager;

    public ScheduleCommentService(ScheduleCommentReader scheduleCommentReader,
                                  ScheduleCommentWriter scheduleCommentWriter,
                                  ScheduleCommentAccessManager scheduleCommentAccessManager) {
        this.scheduleCommentReader = scheduleCommentReader;
        this.scheduleCommentWriter = scheduleCommentWriter;
        this.scheduleCommentAccessManager = scheduleCommentAccessManager;
    }

    public Long add(NewScheduleComment comment) {
        return scheduleCommentWriter.add(comment);
    }

    public List<ScheduleComment> getComments(Schedule schedule) {
        return scheduleCommentReader.readComments(schedule.scheduleId());
    }

    public void delete(Long commentId, Member member) {
        scheduleCommentAccessManager.isCommentAuthor(commentId, member);
        scheduleCommentWriter.delete(commentId);
    }

    public void deleteByScheduleId(Long scheduleId) {
        scheduleCommentWriter.deleteByScheduleId(scheduleId);
    }
}
