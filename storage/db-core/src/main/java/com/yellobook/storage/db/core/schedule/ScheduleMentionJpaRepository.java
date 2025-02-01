package com.yellobook.storage.db.core.schedule;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleMentionJpaRepository extends JpaRepository<ScheduleMentionEntity, Long> {
    List<ScheduleMentionEntity> findAllByScheduleId(Long scheduleId);

    void deleteByScheduleId(Long scheduleId);
}
