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
        teamValidator.canCreateTeam(command.role());
        teamReader.isPresent(command.name());
        Long teamId = teamWriter.create(command.name(), command.description(), command.phoneNumber(), command.address(),
                command.isSearchable());
        teamJoinLeaveManager.join(teamId, memberId, command.role());
        return teamId;
    }

    public void leaveTeam(Long teamId, Long memberId) {
        TeamMemberRole role = teamRoleManager.readRole(teamId, memberId);
        teamJoinLeaveManager.hasJoinedTeam(teamId, memberId);
        Team team = teamReader.read(teamId);
        teamJoinLeaveManager.leaveTeam(teamId, memberId, role);
    }

    public String createInvitationCode(Long teamId, Long memberId, TeamMemberRole inviteRole) {
        TeamMemberRole role = teamRoleManager.readRole(teamId, memberId);
        teamValidator.canCreateInvitationCode(role);
        return teamJoinLeaveManager.generateInvitationCode(teamId, inviteRole);
    }

    public void joinByCode(String key, Member member) {
        InvitationInfo info = teamJoinLeaveManager.getTeamIdByInvitationCode(key);
        teamJoinLeaveManager.hasNotJoinedTeam(info.teamId(), member);
        teamJoinLeaveManager.join(info.teamId(), member.memberId(), info.role());
    }

    public void requestOrdererConversion(Long teamId, Long memberId) {
        TeamMemberRole role = teamRoleManager.readRole(teamId, memberId);
        Team team = teamReader.read(teamId);
        teamValidator.canRequestOrdererConversion(teamId, memberId, role);
        teamRoleManager.requestOrdererConversion(teamId, memberId);
    }

    public void acceptOrdererConversionRequest(Long teamId, Long memberId) {
        TeamMemberRole role = teamRoleManager.readRole(teamId, memberId);
        teamValidator.canChangeTeamRole(role);
        teamValidator.isMemberOfTeam(teamId, memberId);
        teamRoleManager.deleteOrdererConversionRequest(teamId, memberId);
        teamRoleManager.updateRole(teamId, memberId, TeamMemberRole.ORDERER);
    }

    public void rejectOrdererConversionRequest(Long teamId, Long memberId) {
        TeamMemberRole role = teamRoleManager.readRole(teamId, memberId);
        teamValidator.canChangeTeamRole(role);
        teamRoleManager.deleteOrdererConversionRequest(teamId, memberId);
    }

    public List<Team> searchTeamByName(String keyword) {
        return teamReader.readSearchableTeamsByName(keyword);
    }

    public void requestTeamJoin(Long teamId, Long memberId) {
        teamValidator.canJoinTeam(teamId, memberId);
        Team team = teamReader.read(teamId);
        teamJoinLeaveManager.requestTeamJoin(teamId, memberId);
    }

    public void acceptTeamJoinRequest(Long teamId, Long memberId) {
        TeamMemberRole role = teamRoleManager.readRole(teamId, memberId);
        teamValidator.canUpdateTeamJoinRequest(role);
        teamValidator.canJoinTeam(teamId, memberId);
        teamJoinLeaveManager.deleteJoinRequest(teamId, memberId);
        teamJoinLeaveManager.join(teamId, memberId, TeamMemberRole.VIEWER);
    }

    public void rejectTeamJoinRequest(Long teamId, Long memberId) {
        TeamMemberRole role = teamRoleManager.readRole(teamId, memberId);
        teamValidator.canUpdateTeamJoinRequest(role);
        teamJoinLeaveManager.deleteJoinRequest(teamId, memberId);
    }


    public void updateSearchable(Long teamId, Long memberId, boolean searchable) {
        TeamMemberRole role = teamRoleManager.readRole(teamId, memberId);
        Team team = teamReader.read(teamId);
        teamValidator.canModifySearchable(role);
        teamWriter.updateSearchable(teamId, searchable);
    }

    public List<Participant> getParticipants(Long teamId) {
        Team team = teamReader.read(teamId);
        return teamJoinLeaveManager.getParticipants(teamId);
    }

}
