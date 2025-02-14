package com.yellobook.core.domain.StoreAnnouncement;

import com.yellobook.core.domain.member.Member;
import org.springframework.stereotype.Service;

@Service
public class StoreAnnouncementService {
    private final StoreAnnouncementAccessManager announcementAccessManager;
    private final StoreAnnouncementWriter announceWriter;
    private final StoreAnnouncementReader announceReader;
    private final StoreAnnouncementWriter announcementWriter;
    private final StoreAnnouncementReader announcementReader;

    public StoreAnnouncementService(StoreAnnouncementAccessManager announcementAccessManager,
                                    StoreAnnouncementWriter announceWriter,
                                    StoreAnnouncementReader announceReader, StoreAnnouncementWriter announcementWriter,
                                    StoreAnnouncementReader announcementReader) {
        this.announcementAccessManager = announcementAccessManager;
        this.announceWriter = announceWriter;
        this.announceReader = announceReader;
        this.announcementWriter = announcementWriter;
        this.announcementReader = announcementReader;
    }

    public Long create(NewStoreAnnouncement newAnnouncement) {
        announcementAccessManager.isAbleToCreate(newAnnouncement.author(), newAnnouncement.teamId());
        return announceWriter.create(newAnnouncement);
    }

    public StoreAnnouncement read(Member member, Long teamId, Long announcementId) {
        announcementAccessManager.isAbleToRead(member, teamId);
        announcementWriter.increaseView(announcementId);
        return announceReader.read(announcementId);
    }

    public void activatePin(Member member, Long teamId, Long announcementId) {
        announcementAccessManager.isAbleToUpdateStatus(member, teamId);
        announcementReader.isPinExist(teamId);
        announcementWriter.activateAnnouncementPin(announcementId);
    }

    public void deactivatePin(Member member, Long teamId, Long announcementId) {
        announcementAccessManager.isAbleToUpdateStatus(member, teamId);
        announcementWriter.deactivateAnnouncementPin(announcementId);
    }

    public Long delete(Member member, Long announcementId) {
        announcementAccessManager.isAnnouncementAuthor(member, announcementId);
        announcementWriter.delete(announcementId);
        return announcementId;
    }
}
