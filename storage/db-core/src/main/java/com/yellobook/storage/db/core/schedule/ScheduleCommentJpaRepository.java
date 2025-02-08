package com.yellobook.storage.db.core.schedule;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleCommentJpaRepository extends JpaRepository<ScheduleCommentEntity, Long> {
    List<ScheduleCommentEntity> findByScheduleId(Long scheduleId);

    void deleteAllByScheduleId(Long scheduleId);
}
