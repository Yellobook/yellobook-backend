package com.yellobook.core.domain.Announcement;

import com.yellobook.core.domain.member.Member;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementService {
    private final AnnouncementAccessManager announcementAccessManager;
    private final AnnouncementWriter announceWriter;
    private final AnnouncementReader announceReader;
    private final AnnouncementWriter announcementWriter;
    private final AnnouncementReader announcementReader;

    public AnnouncementService(AnnouncementAccessManager announcementAccessManager, AnnouncementWriter announceWriter,
                               AnnouncementReader announceReader, AnnouncementWriter announcementWriter,
                               AnnouncementReader announcementReader) {
        this.announcementAccessManager = announcementAccessManager;
        this.announceWriter = announceWriter;
        this.announceReader = announceReader;
        this.announcementWriter = announcementWriter;
        this.announcementReader = announcementReader;
    }

    public Long create(NewAnnouncement newAnnouncement) {
        announcementAccessManager.isAbleToCreate(newAnnouncement.author(), newAnnouncement.teamId());
        return announceWriter.create(newAnnouncement);
    }

    public Announcement read(Member member, Long teamId, Long announcementId) {
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
}
