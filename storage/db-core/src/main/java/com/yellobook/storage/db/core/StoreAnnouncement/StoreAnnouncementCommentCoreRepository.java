package com.yellobook.storage.db.core.StoreAnnouncement;

import com.yellobook.core.domain.StoreAnnouncement.NewStoreAnnouncementComment;
import com.yellobook.core.domain.StoreAnnouncement.StoreAnnouncementComment;
import com.yellobook.core.domain.StoreAnnouncement.StoreAnnouncementCommentRepository;
import com.yellobook.core.domain.member.Member;
import com.yellobook.storage.db.core.member.MemberEntity;
import com.yellobook.storage.db.core.member.MemberJpaRepository;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StoreAnnouncementCommentCoreRepository implements StoreAnnouncementCommentRepository {

    private final StoreAnnouncementJpaRepository announcementJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final StoreAnnouncementCommentJpaRepository storeAnnouncementCommentJpaRepository;

    public StoreAnnouncementCommentCoreRepository(StoreAnnouncementJpaRepository announcementJpaRepository,
                                                  MemberJpaRepository memberJpaRepository,
                                                  StoreAnnouncementCommentJpaRepository announcementCommentJpaRepository) {
        this.announcementJpaRepository = announcementJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.storeAnnouncementCommentJpaRepository = announcementCommentJpaRepository;
    }

    @Override
    @Transactional
    public Long save(NewStoreAnnouncementComment newAnnounceComment) {
        StoreAnnouncementEntity announcementEntity = announcementJpaRepository.getReferenceById(
                newAnnounceComment.announcementId());
        MemberEntity memberEntity = memberJpaRepository.getReferenceById(newAnnounceComment.member()
                .memberId());
        return storeAnnouncementCommentJpaRepository.save(
                        new StoreAnnouncementCommentEntity(newAnnounceComment.content(), announcementEntity, memberEntity))
                .getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreAnnouncementComment> findByAnnouncementId(Long announcementId) {
        return storeAnnouncementCommentJpaRepository.findByAnnouncementId(announcementId)
                .stream()
                .map(StoreAnnouncementCommentEntity::toStoreAnnouncementComment)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isAnnouncementCommentAuthor(Member member, Long commentId) {
        return storeAnnouncementCommentJpaRepository.getReferenceById(commentId)
                .getMember()
                .getId()
                .equals(member.memberId());
    }

    @Override
    @Transactional
    public void deleteByAnnouncementId(Long announcementId) {
        storeAnnouncementCommentJpaRepository.deleteByAnnouncementId(announcementId);
    }

    @Override
    @Transactional
    public void deleteByCommentId(Long commentId) {
        storeAnnouncementCommentJpaRepository.deleteById(commentId);
    }
}
