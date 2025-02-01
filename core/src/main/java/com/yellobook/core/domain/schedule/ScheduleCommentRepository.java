package com.yellobook.core.domain.schedule;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleCommentRepository {
    Long save(NewScheduleComment commend);

    List<ScheduleComment> findCommentsByScheduleId(Long scheduleId);
}
