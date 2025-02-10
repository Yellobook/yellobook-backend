package com.yellobook.core.domain.schedule;

import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ScheduleReader {
    private final ScheduleRepository scheduleRepository;

    public ScheduleReader(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Schedule read(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CoreException(CoreErrorType.SCHEDULE_NOT_FOUND));
    }

    public Boolean exist(Long scheduleId) {
        return scheduleRepository.existsById(scheduleId);
    }

    public List<ScheduleMemberSearchItem> findMembersByKeyword(String keyword, Long teamId) {
        return scheduleRepository.findMembersByKeywordAndTeamId(keyword, teamId);
    }
}
