package com.yellobook.storage.db.core.schedule;

import com.yellobook.core.domain.schedule.NewScheduleComment;
import com.yellobook.core.domain.schedule.ScheduleComment;
import com.yellobook.core.domain.schedule.ScheduleCommentRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleCommentCoreRepository implements ScheduleCommentRepository {
    @Override
    public Long save(NewScheduleComment commend) {
        return 1L;
    }

    @Override
    public List<ScheduleComment> findCommentsByScheduleId(Long scheduleId) {
        return List.of();
    }
}
