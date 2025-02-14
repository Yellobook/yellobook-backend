package com.yellobook.core.domain.StoreAnnouncement;

import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class StoreAnnouncementReader {

    private final StoreAnnouncementRepository announcementRepository;

    public StoreAnnouncementReader(StoreAnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    public StoreAnnouncement read(Long announcementId) {
        return announcementRepository.getAnnouncementById(announcementId)
                .orElseThrow(() -> new CoreException(CoreErrorType.ANNOUNCEMENT_NOT_FOUND));
    }

    public void isPinExist(Long teamId) {
        if (announcementRepository.isPinExist(teamId)) {
            throw new CoreException(CoreErrorType.PINNED_ANNOUNCEMENT_EXIST);
        }
    }
}
