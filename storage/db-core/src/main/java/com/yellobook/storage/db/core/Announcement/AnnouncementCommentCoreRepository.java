package com.yellobook.storage.db.core.Announcement;

import com.yellobook.core.domain.Announcement.AnnouncementComment;
import com.yellobook.core.domain.Announcement.AnnouncementCommentRepository;
import com.yellobook.core.domain.Announcement.NewAnnouncementComment;
import com.yellobook.core.domain.member.Member;
import com.yellobook.storage.db.core.member.MemberEntity;
import com.yellobook.storage.db.core.member.MemberJpaRepository;
import java.util.List;
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

    @Override
    public List<AnnouncementComment> findByAnnouncementId(Long announcementId) {
        return announcementCommentJpaRepository.findByAnnouncementId(announcementId)
                .stream()
                .map(AnnouncementCommentEntity::toAnnouncementComment)
                .toList();
    }

    @Override
    public Boolean isAnnouncementCommentAuthor(Member member, Long commentId) {
        return announcementCommentJpaRepository.getReferenceById(commentId)
                .getMember()
                .getId()
                .equals(member.memberId());
    }

    @Override
    public void deleteByAnnouncementId(Long announcementId) {
        announcementCommentJpaRepository.deleteByAnnouncementId(announcementId);
    }

    @Override
    public void deleteByCommentId(Long commentId) {
        announcementCommentJpaRepository.deleteById(commentId);
    }
}
