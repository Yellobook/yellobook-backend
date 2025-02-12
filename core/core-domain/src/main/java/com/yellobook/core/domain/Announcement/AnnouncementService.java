package com.yellobook.core.domain.Announcement;

import com.yellobook.core.domain.member.Member;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementService {
    private final AnnouncementAccessManager announcementAccessManager;
    private final AnnouncementWriter announceWriter;
    private final AnnouncementReader announceReader;
    private final AnnouncementWriter announcementWriter;

    public AnnouncementService(AnnouncementAccessManager announcementAccessManager, AnnouncementWriter announceWriter,
                               AnnouncementReader announceReader, AnnouncementWriter announcementWriter) {
        this.announcementAccessManager = announcementAccessManager;
        this.announceWriter = announceWriter;
        this.announceReader = announceReader;
        this.announcementWriter = announcementWriter;
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
}
