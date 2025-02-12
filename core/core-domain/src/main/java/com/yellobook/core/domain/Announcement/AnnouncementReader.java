package com.yellobook.core.domain.Announcement;

import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class AnnouncementReader {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementReader(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    public Announcement read(Long announcementId) {
        return announcementRepository.getAnnouncementById(announcementId)
                .orElseThrow(() -> new CoreException(CoreErrorType.ANNOUNCEMENT_NOT_FOUND));
    }
}
