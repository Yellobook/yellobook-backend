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

    public void increaseView(Long announcementId) {
        announcementRepository.increaseView(announcementId);
    }

    public void deactivateAnnouncementPin(Long announcementId) {
        announcementRepository.deactivateAnnouncementPin(announcementId);
    }

    public void activateAnnouncementPin(Long announcementId) {
        announcementRepository.activateAnnouncementPin(announcementId);
    }

    public void delete(Long announcementId) {
        announcementRepository.delete(announcementId);
    }
}
