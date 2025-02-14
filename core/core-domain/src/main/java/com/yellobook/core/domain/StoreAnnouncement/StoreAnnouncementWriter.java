package com.yellobook.core.domain.StoreAnnouncement;

import org.springframework.stereotype.Component;

@Component
public class StoreAnnouncementWriter {

    private final StoreAnnouncementRepository announcementRepository;

    public StoreAnnouncementWriter(StoreAnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    public Long create(NewStoreAnnouncement newAnnouncement) {
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
