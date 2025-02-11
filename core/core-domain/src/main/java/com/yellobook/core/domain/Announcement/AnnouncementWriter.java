package com.yellobook.core.domain.Announcement;

import org.springframework.stereotype.Component;

@Component
public class AnnouncementWriter {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementWriter(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    public Long create(NewAnnouncement newAnnouncement) {
        return announcementRepository.save(newAnnouncement);
    }
}
