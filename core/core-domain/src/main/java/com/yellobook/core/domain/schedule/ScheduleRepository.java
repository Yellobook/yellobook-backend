package com.yellobook.core.domain.schedule;

import com.yellobook.core.domain.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository {
    Long save(NewSchedule newSchedule);

    void deleteById(Long scheduleId);

    Boolean existsById(Long scheduleId);

    Optional<Schedule> findById(Long scheduleId);

    Boolean isMentioned(Long scheduleId, Member member);

    void increaseView(Long scheduleId);

    void mention(Long scheduleId, List<Long> mentionIds);

    List<ScheduleMention> getMentionsByScheduleId(Long scheduleId);

    List<ScheduleMemberSearchItem> findMembersByKeywordAndTeamId(String keyword, Long teamId);
}
