package com.yellobook.core.domain.StoreAnnouncement;

import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.team.TeamRoleManager;
import com.yellobook.core.domain.team.TeamValidator;
import com.yellobook.core.enums.TeamMemberRole;
import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class StoreAnnouncementAccessManager {
    private final StoreAnnouncementRepository announcementRepository;
    private final TeamRoleManager teamRoleManager;
    private final TeamValidator teamValidator;
    private final StoreAnnouncementReader announcementReader;

    public StoreAnnouncementAccessManager(StoreAnnouncementRepository announcementRepository,
                                          TeamRoleManager teamRoleManager,
                                          TeamValidator teamValidator, StoreAnnouncementReader announcementReader) {
        this.announcementRepository = announcementRepository;
        this.teamRoleManager = teamRoleManager;
        this.teamValidator = teamValidator;
        this.announcementReader = announcementReader;
    }

    public void isAbleToCreate(Member member, Long teamId) {
        if (!teamRoleManager.readRole(teamId, member.memberId())
                .equals(TeamMemberRole.SELLER)) {
            throw new CoreException(CoreErrorType.ONLY_SELLER_CAN_CREATE_ANNOUNCEMENT);
        }
    }

    public void isAbleToRead(Member member, Long teamId) {
        teamValidator.isMemberOfTeam(teamId, member.memberId());
    }

    public void isAbleToUpdateStatus(Member member, Long teamId) {
        if (!teamRoleManager.readRole(teamId, member.memberId())
                .equals(TeamMemberRole.SELLER)) {
            throw new CoreException(CoreErrorType.ONLY_SELLER_CAN_CHANGE_STATUS);
        }
    }

    public void isAnnouncementAuthor(Member member, Long announcementId) {
        if (!announcementReader.read(announcementId)
                .author()
                .equals(member)) {
            throw new CoreException(CoreErrorType.ONLY_AUTHOR_CAN_DELETE);
        }
    }
}
