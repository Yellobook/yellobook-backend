package com.yellobook.core.domain.schedule;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleCommentRepository {
    Long save(NewScheduleComment comment);

    List<ScheduleComment> findCommentsByScheduleId(Long scheduleId);

    void deleteByCommentId(Long commentId);

    ScheduleComment findByCommentId(Long commentId);

    void deleteCommentsByScheduleId(Long scheduleId);
}
