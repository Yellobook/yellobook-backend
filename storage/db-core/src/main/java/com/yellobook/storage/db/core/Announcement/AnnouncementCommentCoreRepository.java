package com.yellobook.storage.db.core.Announcement;

import com.yellobook.core.domain.Announcement.AnnouncementCommentRepository;
import com.yellobook.core.domain.Announcement.NewAnnouncementComment;
import com.yellobook.storage.db.core.member.MemberEntity;
import com.yellobook.storage.db.core.member.MemberJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class AnnouncementCommentCoreRepository implements AnnouncementCommentRepository {

    private final AnnouncementJpaRepository announcementJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final AnnouncementCommentJpaRepository announcementCommentJpaRepository;

    public AnnouncementCommentCoreRepository(AnnouncementJpaRepository announcementJpaRepository,
                                             MemberJpaRepository memberJpaRepository,
                                             AnnouncementCommentJpaRepository announcementCommentJpaRepository) {
        this.announcementJpaRepository = announcementJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.announcementCommentJpaRepository = announcementCommentJpaRepository;
    }

    @Override
    public Long save(NewAnnouncementComment newAnnounceComment) {
        AnnouncementEntity announcementEntity = announcementJpaRepository.getReferenceById(
                newAnnounceComment.announcementId());
        MemberEntity memberEntity = memberJpaRepository.getReferenceById(newAnnounceComment.member()
                .memberId());
        return announcementCommentJpaRepository.save(
                        new AnnouncementCommentEntity(newAnnounceComment.content(), announcementEntity, memberEntity))
                .getId();
    }
}
