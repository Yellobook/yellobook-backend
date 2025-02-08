package com.yellobook.storage.db.core.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, Long> {
    @Modifying
    @Query("UPDATE ScheduleEntity s SET s.view = s.view +1 WHERE s.id = :scheduleId")
    void increaseView(@Param("scheduleId") Long scheduleId);
}
