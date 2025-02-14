package com.yellobook.core.domain.StoreAnnouncement;

import com.yellobook.core.domain.member.Member;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreAnnouncementCommentRepository {
    Long save(NewStoreAnnouncementComment newAnnounceComment);

    List<StoreAnnouncementComment> findByAnnouncementId(Long announcementId);

    Boolean isAnnouncementCommentAuthor(Member member, Long commentId);

    void deleteByAnnouncementId(Long announcementId);

    void deleteByCommentId(Long commentId);
}
