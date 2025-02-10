package com.yellobook.storage.db.core.schedule;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.schedule.NewSchedule;
import com.yellobook.core.domain.schedule.Schedule;
import com.yellobook.core.domain.schedule.ScheduleMemberSearchItem;
import com.yellobook.core.domain.schedule.ScheduleMention;
import com.yellobook.core.domain.schedule.ScheduleRepository;
import com.yellobook.storage.db.core.member.MemberEntity;
import com.yellobook.storage.db.core.member.MemberJpaRepository;
import com.yellobook.storage.db.core.member.QMemberEntity;
import com.yellobook.storage.db.core.team.QParticipantEntity;
import com.yellobook.storage.db.core.team.TeamEntity;
import com.yellobook.storage.db.core.team.TeamJpaRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleCoreRepository implements ScheduleRepository {
    private final ScheduleJpaRepository scheduleJpaRepository;
    private final ScheduleMentionJpaRepository scheduleMentionJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final TeamJpaRepository teamJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public ScheduleCoreRepository(ScheduleJpaRepository scheduleJpaRepository,
                                  ScheduleMentionJpaRepository scheduleMentionJpaRepository,
                                  MemberJpaRepository memberJpaRepository,
                                  TeamJpaRepository teamJpaRepository,
                                  JPAQueryFactory jpaQueryFactory) {
        this.scheduleJpaRepository = scheduleJpaRepository;
        this.scheduleMentionJpaRepository = scheduleMentionJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.teamJpaRepository = teamJpaRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Long save(NewSchedule newSchedule) {
        MemberEntity author = memberJpaRepository.getReferenceById(newSchedule.author()
                .memberId());
        TeamEntity team = teamJpaRepository.getReferenceById(newSchedule.teamId());
        return scheduleJpaRepository.save(
                        new ScheduleEntity(
                                newSchedule.title(),
                                newSchedule.content(),
                                newSchedule.plannedDate(),
                                author,
                                team
                        ))
                .getId();
    }

    @Override
    public void deleteById(Long scheduleId) {
        scheduleJpaRepository.deleteById(scheduleId);
        scheduleMentionJpaRepository.deleteByScheduleId(scheduleId);
    }

    @Override
    public Boolean existsById(Long scheduleId) {
        return scheduleJpaRepository.existsById(scheduleId);
    }

    @Override
    public Optional<Schedule> findById(Long scheduleId) {
        return scheduleJpaRepository.findById(scheduleId)
                .map(ScheduleEntity::toSchedule);
    }

    @Override
    public Boolean isMentioned(Long scheduleId, Member member) {
        return scheduleMentionJpaRepository.existsByScheduleIdAndMemberId(scheduleId, member.memberId());
    }

    @Override
    @Transactional
    public void increaseView(Long scheduleId) {
        scheduleJpaRepository.increaseView(scheduleId);
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

    @Override
    public List<ScheduleMention> getMentionsByScheduleId(Long scheduleId) {
        return scheduleMentionJpaRepository.findAllByScheduleId(scheduleId)
                .stream()
                .map(ScheduleMentionEntity::toScheduleMention)
                .toList();
    }

    @Override
    public List<ScheduleMemberSearchItem> findMembersByKeywordAndTeamId(String keyword, Long teamId) {
        QMemberEntity memberEntity = QMemberEntity.memberEntity;
        QParticipantEntity participantEntity = QParticipantEntity.participantEntity;

        return jpaQueryFactory.select(
                        Projections.constructor(
                                ScheduleMemberSearchItem.class,
                                memberEntity.id,
                                memberEntity.nickname,
                                memberEntity.profileImage))
                .from(participantEntity)
                .join(participantEntity.member, memberEntity)
                .where(
                        participantEntity.team.id.eq(teamId),
                        memberEntity.isDeleted.eq(false),
                        (keyword != null && !keyword.isEmpty()) ?
                                memberEntity.nickname.startsWithIgnoreCase(keyword) : null
                )
                .fetch();
    }
}
