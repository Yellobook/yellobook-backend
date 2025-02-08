package com.yellobook.storage.db.core.schedule;

import com.yellobook.core.domain.schedule.NewScheduleComment;
import com.yellobook.core.domain.schedule.ScheduleComment;
import com.yellobook.core.domain.schedule.ScheduleCommentRepository;
import com.yellobook.storage.db.core.member.MemberEntity;
import com.yellobook.storage.db.core.member.MemberJpaRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleCommentCoreRepository implements ScheduleCommentRepository {
    private final ScheduleCommentJpaRepository scheduleCommentJpaRepository;
    private final ScheduleJpaRepository scheduleJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    public ScheduleCommentCoreRepository(ScheduleCommentJpaRepository scheduleCommentJpaRepository,
                                         ScheduleJpaRepository scheduleJpaRepository,
                                         MemberJpaRepository memberJpaRepository) {
        this.scheduleCommentJpaRepository = scheduleCommentJpaRepository;
        this.scheduleJpaRepository = scheduleJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    @Transactional
    public Long save(NewScheduleComment comment) {
        MemberEntity commenter = memberJpaRepository.getReferenceById(comment.commenterId());
        ScheduleEntity schedule = scheduleJpaRepository.getReferenceById(comment.scheduleId());
        return scheduleCommentJpaRepository.save(
                        new ScheduleCommentEntity(comment.content(), commenter, schedule))
                .getId();
    }

    @Override
    public List<ScheduleComment> findCommentsByScheduleId(Long scheduleId) {
        return scheduleCommentJpaRepository.findByScheduleId(scheduleId)
                .stream()
                .map(ScheduleCommentEntity::toScheduleComment)
                .toList();
    }

    @Override
    @Transactional
    public void deleteByCommentId(Long commentId) {
        scheduleCommentJpaRepository.deleteById(commentId);
    }

    @Override
    public ScheduleComment findByCommentId(Long commentId) {
        return scheduleCommentJpaRepository.getReferenceById(commentId)
                .toScheduleComment();
    }

    @Override
    @Transactional
    public void deleteCommentsByScheduleId(Long scheduleId) {
        scheduleCommentJpaRepository.deleteAllByScheduleId(scheduleId);
    }
}
