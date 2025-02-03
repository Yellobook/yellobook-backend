package com.yellobook.core.domain.schedule;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository {
    Long save(NewSchedule newSchedule);

    void deleteById(Long scheduleId);

    Boolean existsById(Long scheduleId);

    Optional<Schedule> findById(Long scheduleId);

    Boolean isMentioned(Long scheduleId, Long memberId);

    void increaseView(Long scheduleId);

    void mention(Long scheduleId, List<Long> mentionIds);
}
