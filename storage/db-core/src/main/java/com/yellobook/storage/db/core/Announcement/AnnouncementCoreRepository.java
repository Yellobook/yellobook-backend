package com.yellobook.storage.db.core.Announcement;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.core.domain.Announcement.Announcement;
import com.yellobook.core.domain.Announcement.AnnouncementRepository;
import com.yellobook.core.domain.Announcement.NewAnnouncement;
import com.yellobook.core.domain.member.Member;
import com.yellobook.core.enums.AnnouncementStatus;
import com.yellobook.storage.db.core.member.MemberEntity;
import com.yellobook.storage.db.core.member.MemberJpaRepository;
import com.yellobook.storage.db.core.team.TeamEntity;
import com.yellobook.storage.db.core.team.TeamJpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class AnnouncementCoreRepository implements AnnouncementRepository {

    private final AnnouncementJpaRepository announcementJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final TeamJpaRepository teamJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public AnnouncementCoreRepository(AnnouncementJpaRepository announcementJpaRepository,
                                      MemberJpaRepository memberJpaRepository, TeamJpaRepository teamJpaRepository,
                                      JPAQueryFactory jpaQueryFactory) {
        this.announcementJpaRepository = announcementJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.teamJpaRepository = teamJpaRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Long save(NewAnnouncement newAnnouncement) {
        MemberEntity author = memberJpaRepository.getReferenceById(newAnnouncement.author()
                .memberId());
        TeamEntity team = teamJpaRepository.getReferenceById(newAnnouncement.teamId());
        return announcementJpaRepository.save(
                        new AnnouncementEntity(newAnnouncement.title(), newAnnouncement.content(), author, team))
                .getId();
    }

    @Override
    public Boolean isAnnouncementAuthor(Member member, Long announcementId) {
        return announcementJpaRepository.getReferenceById(announcementId)
                .getMember()
                .toMember()
                .equals(member);
    }

    @Override
    public Optional<Announcement> getAnnouncementById(Long announcementId) {
        return announcementJpaRepository.findById(announcementId)
                .map(AnnouncementEntity::toAnnouncement);
    }

    @Override
    public void increaseView(Long announcementId) {
        announcementJpaRepository.increaseView(announcementId);
    }

    @Override
    public void deactivateAnnouncementPin(Long announcementId) {
        announcementJpaRepository.deactivate(announcementId);
    }

    @Override
    public void activateAnnouncementPin(Long announcementId) {
        announcementJpaRepository.activate(announcementId);
    }

    @Override
    public Boolean isPinExist(Long teamId) {
        QAnnouncementEntity announcement = QAnnouncementEntity.announcementEntity;
        return jpaQueryFactory
                .selectOne()
                .from(announcement)
                .where(
                        announcement.team.id.eq(teamId)
                                .and(announcement.status.eq(AnnouncementStatus.ACTIVE))
                )
                .fetchFirst() != null;
    }

    @Override
    public void delete(Long announcementId) {
        announcementJpaRepository.deleteById(announcementId);
    }
}
