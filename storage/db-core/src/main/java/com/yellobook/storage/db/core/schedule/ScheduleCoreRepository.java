package com.yellobook.storage.db.core.schedule;

import com.yellobook.core.domain.schedule.NewSchedule;
import com.yellobook.core.domain.schedule.Schedule;
import com.yellobook.core.domain.schedule.ScheduleRepository;
import com.yellobook.storage.db.core.member.MemberEntity;
import com.yellobook.storage.db.core.member.MemberJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleCoreRepository implements ScheduleRepository {
    private final ScheduleJpaRepository scheduleJpaRepository;
    private final ScheduleMentionJpaRepository scheduleMentionJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    public ScheduleCoreRepository(ScheduleJpaRepository scheduleJpaRepository,
                                  ScheduleMentionJpaRepository scheduleMentionJpaRepository,
                                  MemberJpaRepository memberJpaRepository) {
        this.scheduleJpaRepository = scheduleJpaRepository;
        this.scheduleMentionJpaRepository = scheduleMentionJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
    }

    //TODO: teamId 수정
    @Override
    public Long save(NewSchedule newSchedule) {
        return scheduleJpaRepository.save(
                        new ScheduleEntity(newSchedule.title(), newSchedule.memo(), newSchedule.plannedDate(), 1L,
                                newSchedule.author()
                                        .memberId()))
                .getId();
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

    @Override
    public void mention(Long scheduleId, List<Long> mentionIds) {
        ScheduleEntity schedule = scheduleJpaRepository.getReferenceById(scheduleId);
        List<MemberEntity> mentionedMembers = memberJpaRepository.findAllById(mentionIds);

        List<ScheduleMentionEntity> scheduleMentions = mentionedMembers.stream()
                .map(member -> new ScheduleMentionEntity(member, schedule))
                .toList();
        scheduleMentionJpaRepository.saveAll(scheduleMentions);
    }
}
