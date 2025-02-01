package com.yellobook.storage.db.core.schedule;

import com.yellobook.core.domain.schedule.NewSchedule;
import com.yellobook.core.domain.schedule.Schedule;
import com.yellobook.core.domain.schedule.ScheduleRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleCoreRepository implements ScheduleRepository {
    @Override
    public Long save(NewSchedule newSchedule) {
        return 1L;
    }

    @Override
    public void deleteById(Long scheduleId) {

    }

    @Override
    public Boolean existsById(Long scheduleId) {
        return null;
    }

    @Override
    public Optional<Schedule> findById(Long scheduleId) {
        return Optional.empty();
    }

    @Override
    public Boolean isMentioned(Long scheduleId, Long memberId) {
        return null;
    }

    @Override
    public void increaseView(Long scheduleId) {

    }
}
