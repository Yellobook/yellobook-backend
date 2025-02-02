package com.yellobook.storage.db.core.schedule;

import com.yellobook.core.domain.schedule.NewScheduleComment;
import com.yellobook.core.domain.schedule.ScheduleComment;
import com.yellobook.core.domain.schedule.ScheduleCommentRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleCommentCoreRepository implements ScheduleCommentRepository {
    private final ScheduleCommentJpaRepository scheduleCommentJpaRepository;

    public ScheduleCommentCoreRepository(ScheduleCommentJpaRepository scheduleCommentJpaRepository) {
        this.scheduleCommentJpaRepository = scheduleCommentJpaRepository;
    }

    @Override
    public Long save(NewScheduleComment comment) {
        return 1L;
    }

    @Override
    public List<ScheduleComment> findCommentsByScheduleId(Long scheduleId) {
        return List.of();
    }
}
