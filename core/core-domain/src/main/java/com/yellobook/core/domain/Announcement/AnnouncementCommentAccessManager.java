package com.yellobook.core.domain.Announcement;

import com.yellobook.core.domain.member.Member;
import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class AnnouncementCommentAccessManager {

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementCommentRepository announcementCommentRepository;

    public AnnouncementCommentAccessManager(AnnouncementRepository announcementRepository,
                                            AnnouncementCommentRepository announcementCommentRepository) {
        this.announcementRepository = announcementRepository;
        this.announcementCommentRepository = announcementCommentRepository;
    }

    public void isAbleToCreateComment(Member member, Long announcementId) {
        if (!announcementRepository.isAnnouncementAuthor(member, announcementId)) {
            throw new CoreException(CoreErrorType.ONLY_AUTHOR_CAN_COMMENT);
        }
    }

    public void isCommentAuthor(Member member, Long commentId) {
        if (!announcementCommentRepository.isAnnouncementCommentAuthor(member, commentId)) {
            throw new CoreException(CoreErrorType.ONLY_COMMENTER_CAN_DELETE);
        }
    }
}
