package com.yellobook.core.domain.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.team.dto.CreateTeamCommand;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    private final TeamReader teamReader;
    private final TeamWriter teamWriter;
    private final TeamValidator teamValidator;
    private final TeamApplyManager teamApplyManager;
    private final TeamRoleManager teamRoleManager;

    public TeamService(TeamReader teamReader, TeamWriter teamWriter, TeamValidator teamValidator,
                       TeamApplyManager teamApplyManager, TeamRoleManager teamRoleManager) {
        this.teamReader = teamReader;
        this.teamWriter = teamWriter;
        this.teamValidator = teamValidator;
        this.teamApplyManager = teamApplyManager;
        this.teamRoleManager = teamRoleManager;
    }

    /**
     * 팀 생성 팀은 판매자만 생성할 수 있다.
     *
     * @param command  생성할 팀 정보
     * @param memberId 팀 생성자
     * @return 생성된 팀 id
     */
    @Transactional
    public Long create(CreateTeamCommand command, Long memberId) {
        teamValidator.canCreateTeam(command.role());
        teamReader.isPresent(command.name());
        Long teamId = teamWriter.create(command.name(), command.phoneNumber(), command.address(), command.searchable());
        teamWriter.join(teamId, memberId, command.role());
        teamApplyManager.setMemberCurrentTeam(teamId, memberId, command.role());
        return teamId;
    }

    /*
    팀 나가기
     */
    @Transactional
    public void leaveTeam(Long teamId, Long memberId, TeamMemberRole role) {
        teamReader.validateParticipant(teamId, memberId);
        Team team = teamReader.read(teamId);
        teamWriter.leaveTeam(teamId, memberId, role);
    }

    /*
    초대 코드 발행
     */
    public String createInvitationCode(Long teamId, TeamMemberRole role) {
        teamValidator.canCreateInvitationCode(role);
        return teamWriter.createInvitationCode(teamId);
    }

    /**
     * 초대 코드를 통한 팀 참가 초대 링크를 통해 가입한 사용자의 권한은 뷰어이다.
     *
     * @param key    초대 코드
     * @param member 참여자
     */
    @Transactional
    public void joinByCode(String key, Member member) {
        Long teamId = teamApplyManager.read(key);
        teamReader.validateNotParticipant(teamId, member);
        teamWriter.join(teamId, member.memberId(), TeamMemberRole.VIEWER);
        teamApplyManager.setMemberCurrentTeam(teamId, member.memberId(), TeamMemberRole.VIEWER);
    }

    /*
    팀 전환 (..?)
     */
    public Team convertTeam(Long teamId, Member member) {
        teamReader.validateParticipant(teamId, member.memberId());
        teamApplyManager.setMemberCurrentTeam(teamId, member.memberId(), teamReader.readRole(teamId, member));
        return teamReader.read(teamId);
    }

    /**
     * 뷰어 -> 주문자 전환 요청
     *
     * @param teamId   팀 Id
     * @param memberId 사용자 Id
     */
    public void requestOrdererConversion(Long teamId, Long memberId, TeamMemberRole role) {
        Team team = teamReader.read(teamId);
        teamValidator.canRequestOrdererConversion(teamId, memberId, role);
        teamRoleManager.requestOrdererConversion(teamId, memberId);
    }

    /**
     * 뷰어 -> 주문자 전환 요청 승인 관리자만 할 수 있다.
     */
    public void acceptOrdererConversionRequest(Long teamId, Long memberId, TeamMemberRole role) {
        teamValidator.canChangeTeamRole(role);
        teamValidator.isMemberOfTeam(teamId, memberId);
        teamRoleManager.deleteOrdererConversionRequest(teamId, memberId);
        teamWriter.updateRole(teamId, memberId, TeamMemberRole.ORDERER);
    }

    /**
     * 뷰어 -> 주문자 전환 요청 거절 관리자만 할 수 있다.
     */
    public void rejectOrdererConversionRequest(Long teamId, Long memberId, TeamMemberRole role) {
        teamValidator.canChangeTeamRole(role);
        teamRoleManager.deleteOrdererConversionRequest(teamId, memberId);
    }

    /**
     * 팀 이름 검색 해당 키워드가 포함되어 있는 팀 이름을 검색한다. 검색 허용인 팀 목록만 검색이 가능하다.
     *
     * @param keyword 키워드
     * @return 해당 키워드를 팀 이름에 가지고 있는 팀 리스트
     */
    public List<Team> searchTeamByName(String keyword) {
        return teamReader.readPublicTeamsByName(keyword);
    }

    /**
     * 팀 가입 요청
     *
     * @param teamId   가입하고 싶은 팀 Id
     * @param memberId 가입하고자 하는 사람의 Id
     */
    public void requestTeamJoin(Long teamId, Long memberId) {
        teamValidator.canJoinTeam(teamId, memberId);
        Team team = teamReader.read(teamId);
        teamApplyManager.requestTeamJoin(teamId, memberId);
    }

    /**
     * 팀 가입 요청 승인 관리자 또는 주문자만 승인 가능하다 팀의 가입한 사용자의 권한은 뷰어이다.
     */
    public void acceptTeamJoinRequest(Long teamId, Long memberId, TeamMemberRole role) {
        teamValidator.canUpdateTeamJoinRequest(role);
        teamValidator.canJoinTeam(teamId, memberId);
        teamApplyManager.deleteJoinRequest(teamId, memberId);
        teamWriter.join(teamId, memberId, TeamMemberRole.VIEWER);
    }

    /**
     * 팀 가입 요청 거절 관리자 또는 주문자만 거절 가능하다
     */
    public void rejectTeamJoinRequest(Long teamId, Long memberId, TeamMemberRole role) {
        teamValidator.canUpdateTeamJoinRequest(role);
        teamApplyManager.deleteJoinRequest(teamId, memberId);
    }

    /**
     * 팀의 검색 허용 여부를 수정 관리자만 수정가능하다.
     *
     * @param teamId     팀 id
     * @param role       수정하는 사람의 역할
     * @param searchable 어떤 검색 여부로 변경할 것인지
     */
    public void updateSearchable(Long teamId, TeamMemberRole role, Searchable searchable) {
        Team team = teamReader.read(teamId);
        teamValidator.canModifySearchable(role);
        teamWriter.updateSearchable(teamId, searchable);
    }

    public List<Participant> getParticipants(Long teamId) {
        Team team = teamReader.read(teamId);
        return teamReader.getParticipants(teamId);
    }

}
