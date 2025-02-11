package com.yellobook.storage.db.core.Announcement;

import com.yellobook.core.domain.Announcement.AnnouncementRepository;
import com.yellobook.core.domain.Announcement.NewAnnouncement;
import com.yellobook.core.domain.member.Member;
import com.yellobook.storage.db.core.member.MemberEntity;
import com.yellobook.storage.db.core.member.MemberJpaRepository;
import com.yellobook.storage.db.core.team.TeamEntity;
import com.yellobook.storage.db.core.team.TeamJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class AnnouncementCoreRepository implements AnnouncementRepository {

    private final AnnouncementJpaRepository announcementJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final TeamJpaRepository teamJpaRepository;

    public AnnouncementCoreRepository(AnnouncementJpaRepository announcementJpaRepository,
                                      MemberJpaRepository memberJpaRepository, TeamJpaRepository teamJpaRepository) {
        this.announcementJpaRepository = announcementJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.teamJpaRepository = teamJpaRepository;
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
}
