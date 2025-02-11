package com.yellobook.core.domain.Announcement;

import org.springframework.stereotype.Service;

@Service
public class AnnouncementService {
    private final AnnouncementAccessManager announcementAccessManager;
    private final AnnouncementWriter announceWriter;
    private final AnnouncementReader announceReader;

    public AnnouncementService(AnnouncementAccessManager announcementAccessManager, AnnouncementWriter announceWriter,
                               AnnouncementReader announceReader) {
        this.announcementAccessManager = announcementAccessManager;
        this.announceWriter = announceWriter;
        this.announceReader = announceReader;
    }

    Long create(NewAnnouncement newAnnouncement) {
        announcementAccessManager.isAbleToCreate(newAnnouncement.author(), newAnnouncement.teamId());
        return announceWriter.create(newAnnouncement);
    }
}
