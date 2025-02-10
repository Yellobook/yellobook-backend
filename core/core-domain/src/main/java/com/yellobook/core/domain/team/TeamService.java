package com.yellobook.core.domain.team;

import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.team.dto.CreateTeamCommand;
import com.yellobook.core.domain.team.dto.InvitationInfo;
import com.yellobook.core.enums.TeamMemberRole;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TeamService {
    private final TeamReader teamReader;
    private final TeamWriter teamWriter;
    private final TeamValidator teamValidator;
    private final TeamRoleManager teamRoleManager;
    private final TeamJoinLeaveManager teamJoinLeaveManager;

    public TeamService(TeamReader teamReader, TeamWriter teamWriter, TeamValidator teamValidator,
                       TeamRoleManager teamRoleManager, TeamJoinLeaveManager teamJoinLeaveManager) {
        this.teamReader = teamReader;
        this.teamWriter = teamWriter;
        this.teamValidator = teamValidator;
        this.teamRoleManager = teamRoleManager;
        this.teamJoinLeaveManager = teamJoinLeaveManager;
    }

    public Long create(CreateTeamCommand command, Long memberId) {
        teamReader.isTeamNameExist(command.name());
        Long teamId = teamWriter.create(command.name(), command.description(), command.phoneNumber(), command.address(),
                command.searchable());
        teamJoinLeaveManager.join(teamId, memberId, TeamMemberRole.SELLER);
        return teamId;
    }

    public Team getTeam(Long teamId, Long memberId) {
        Team team = teamReader.read(teamId);
        if (!team.isSearchable()) {
            teamValidator.isMemberOfTeam(teamId, memberId);
        }
        return team;
    }

    public List<Team> searchTeamByName(String keyword) {
        return teamReader.readSearchableTeamsByName(keyword);
    }

    public void leaveTeam(Long teamId, Long memberId) {
        Team team = teamReader.read(teamId);
        TeamMemberRole role = teamRoleManager.readRole(teamId, memberId);
        teamJoinLeaveManager.leaveTeam(teamId, memberId, role);
    }

    public String createInvitationCode(Long teamId, Long memberId, TeamMemberRole inviteRole) {
        TeamMemberRole role = teamRoleManager.readRole(teamId, memberId);
        teamValidator.canCreateInvitationCode(role);
        teamValidator.canInviteWithRole(role);
        return teamJoinLeaveManager.generateInvitationCode(teamId, inviteRole);
    }

    public Long joinByCode(String code, Long memberId) {
        InvitationInfo info = teamJoinLeaveManager.getTeamIdByInvitationCode(code);
        teamValidator.canJoinTeam(info.teamId(), memberId);
        teamJoinLeaveManager.join(info.teamId(), memberId, info.role());
        return info.teamId();
    }

    public void requestOrdererConversion(Long teamId, Long memberId) {
        TeamMemberRole role = teamRoleManager.readRole(teamId, memberId);
        teamValidator.canRequestOrdererConversion(teamId, memberId, role);
        teamRoleManager.requestOrdererConversion(teamId, memberId);
    }

    public void changeRoleToOrderer(Long teamId, Long requesterId, Member member, Boolean approve) {
        TeamMemberRole role = teamRoleManager.readRole(teamId, member.memberId());
        teamValidator.canChangeTeamRole(role);
        teamRoleManager.deleteOrdererConversionRequest(teamId, requesterId);
        if (approve) {
            teamValidator.isMemberOfTeam(teamId, requesterId);
            teamRoleManager.updateRole(teamId, requesterId, TeamMemberRole.ORDERER);
        }
    }

    public void requestTeamJoin(Long teamId, Long memberId) {
        Team team = teamReader.read(teamId);
        teamValidator.canJoinTeam(teamId, memberId);
        teamJoinLeaveManager.requestTeamJoin(teamId, memberId);
    }

    public void manageTeamJoinRequest(Long teamId, Long requesterId, Member member, Boolean approve) {
        TeamMemberRole role = teamRoleManager.readRole(teamId, member.memberId());
        teamValidator.canUpdateTeamJoinRequest(role);
        teamJoinLeaveManager.deleteJoinRequest(teamId, requesterId);
        if (approve) {
            teamValidator.canJoinTeam(teamId, requesterId);
            teamJoinLeaveManager.join(teamId, requesterId, TeamMemberRole.VIEWER);
        }
    }

    public void updateSearchable(Long teamId, Long memberId, boolean searchable) {
        Team team = teamReader.read(teamId);
        TeamMemberRole role = teamRoleManager.readRole(teamId, memberId);
        teamValidator.canModifySearchable(role);
        teamWriter.updateSearchable(teamId, searchable);
    }

    public List<Participant> getParticipants(Long teamId, Long memberId) {
        teamValidator.isMemberOfTeam(teamId, memberId);
        return teamReader.getParticipants(teamId);
    }
}
