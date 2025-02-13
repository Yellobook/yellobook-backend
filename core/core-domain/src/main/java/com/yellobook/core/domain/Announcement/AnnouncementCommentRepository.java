package com.yellobook.core.domain.Announcement;

import com.yellobook.core.domain.member.Member;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementCommentRepository {
    Long save(NewAnnouncementComment newAnnounceComment);

    List<AnnouncementComment> findByAnnouncementId(Long announcementId);

    Boolean isAnnouncementCommentAuthor(Member member, Long commentId);

    void deleteByAnnouncementId(Long announcementId);

    void deleteByCommentId(Long commentId);
}
