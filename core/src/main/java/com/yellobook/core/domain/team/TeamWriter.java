package com.yellobook.core.domain.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.team.dto.CreateInvitationCodeCommand;
import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class TeamWriter {
    private final TeamRepository teamRepository;
    private final TeamReader teamReader;
    private final TeamCachedManager teamRedisManager;
    private final TeamRoleVerifier teamRoleVerifier;

    public TeamWriter(TeamRepository teamRepository,
                      TeamReader teamReader,
                      TeamCachedManager invitationManager,
                      TeamRoleVerifier teamRoleVerifier) {
        this.teamRepository = teamRepository;
        this.teamReader = teamReader;
        this.teamRedisManager = invitationManager;
        this.teamRoleVerifier = teamRoleVerifier;
    }

    /*
    팀 생성
     */
    public Long create(String name, String phoneNumber, String address) {
        return teamRepository.save(name, phoneNumber, address);
    }

    /*
    팀 참가
     */
    public void join(Long teamId, Member member, TeamMemberRole role) {
        if (role == TeamMemberRole.ADMIN && teamRoleVerifier.hasAdmin(teamId)) {
            throw new CoreException(CoreErrorType.ADMIN_EXISTS);
        }
        teamRepository.join(teamId, member, role);
    }

    /*
    팀 나가기
     */
    public void leave(Long teamId, Member member) {
        teamRepository.leave(teamId, member);
    }

    /*
    팀 초대 코드 발행
     */
    public String createInvitationCode(CreateInvitationCodeCommand command) {
        return teamRedisManager.generateInvitationUrl(command.teamId(), command.role());
    }
}
