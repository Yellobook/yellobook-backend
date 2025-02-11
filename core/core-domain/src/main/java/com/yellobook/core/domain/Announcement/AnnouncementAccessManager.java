package com.yellobook.core.domain.Announcement;

import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.team.TeamRoleManager;
import com.yellobook.core.domain.team.TeamValidator;
import com.yellobook.core.enums.TeamMemberRole;
import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class AnnouncementAccessManager {
    private final AnnouncementRepository announcementRepository;
    private final TeamRoleManager teamRoleManager;
    private final TeamValidator teamValidator;

    public AnnouncementAccessManager(AnnouncementRepository announcementRepository, TeamRoleManager teamRoleManager,
                                     TeamValidator teamValidator) {
        this.announcementRepository = announcementRepository;
        this.teamRoleManager = teamRoleManager;
        this.teamValidator = teamValidator;
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
}
