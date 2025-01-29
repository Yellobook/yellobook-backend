package com.yellobook.core.domain.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.member.MemberReader;
import com.yellobook.core.domain.team.dto.CreateInvitationCodeCommand;
import com.yellobook.core.domain.team.dto.CreateTeamCommand;
import com.yellobook.core.domain.team.dto.InvitationCodeInfo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
    private final TeamReader teamReader;
    private final TeamWriter teamWriter;
    private final TeamValidator teamValidator;
    private final TeamCachedManager teamCachedManager;
    private final MemberReader memberReader;

    public TeamService(TeamReader teamReader, TeamWriter teamWriter, TeamValidator teamValidator, TeamCachedManager teamCachedManager, MemberReader memberReader) {
        this.teamReader = teamReader;
        this.teamWriter = teamWriter;
        this.teamValidator = teamValidator;
        this.teamCachedManager = teamCachedManager;
        this.memberReader = memberReader;
    }

    /**
     * 팀 생성
     * 팀은 판매자만 생성할 수 있다.
     *
     * @param command 생성할 팀 정보
     * @param member  팀 생정자
     * @return 생성된 팀 id
     */
    @Transactional
    public Long create(CreateTeamCommand command, Member member) {
        teamValidator.canCreateTeam(command.role());
        teamReader.isPresent(command.name());
        Long teamId = teamWriter.create(command.name(), command.phoneNumber(), command.address(), command.searchable());
        teamWriter.join(teamId, member, command.role());
        teamCachedManager.setMemberCurrentTeam(teamId, member.memberId(), command.role());
        return teamId;
    }

    /*
    팀 나가기
     */
    @Transactional
    public void leave(Long teamId, Member member) {
        teamReader.validateParticipant(teamId, member);
        teamWriter.leave(teamId, member);
        //teamCachedManager.setMemberCurrentTeam();
    }

    /**
     * 초대 코드를 통한 팀 참가
     * 초대 링크를 통해 가입한 사용자의 권한은 뷰어이다.
     *
     * @param key    초대 코드
     * @param member 참여자
     */
    @Transactional
    public void join(String key, Member member) {
        InvitationCodeInfo info = teamCachedManager.read(key);
        teamReader.validateNotParticipant(info.teamId(), member);
        teamWriter.join(info.teamId(), member, TeamMemberRole.VIEWER);
        teamCachedManager.setMemberCurrentTeam(info.teamId(), member.memberId(), TeamMemberRole.VIEWER);
    }

    /*
    초대 코드 발행
     */
    public String createInvitationCode(CreateInvitationCodeCommand command, Member member) {
        teamReader.validateParticipant(command.teamId(), member);
        teamReader.invitationRoleValidator(command.teamId(), command.role(), member);
        return teamWriter.createInvitationCode(command);
    }

    /*
    팀 전환
     */
    public Team convert(Long teamId, Member member) {
        teamReader.validateParticipant(teamId, member);
        teamCachedManager.setMemberCurrentTeam(teamId, member.memberId(), teamReader.readRole(teamId, member));
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
        teamWriter.requestOrdererConversion(teamId, memberId);
    }

    /**
     * 뷰어 -> 주문자 전환 요청 승인
     * 관리자만 할 수 있다.
     *
     * @param requestId 전환 요청 id
     * @param role
     */
    public void acceptOrdererConversionRequest(Long requestId, TeamMemberRole role) {
        teamValidator.canChangeTeamRole(role);
        RoleConversionInfo roleConversionInfo = teamReader.readRoleConversionInfo(requestId);
        teamWriter.acceptOrdererConversion(roleConversionInfo);
    }

    /**
     * 뷰어 -> 주문자 전환 요청 거절
     * 관리자만 할 수 있다.
     *
     * @param requestId 전환 요청 id
     */
    public void rejectOrdererConversionRequest(Long requestId, TeamMemberRole role) {
        teamValidator.canChangeTeamRole(role);
        teamWriter.rejectOrdererConversion(requestId);
    }

    /**
     * 팀 이름 검색
     * 해당 키워드가 포함되어 있는 팀 이름을 검색한다.
     * 검색 허용인 팀 목록만 검색이 가능하다.
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
        teamValidator.canApplyTeam(teamId, memberId);
        Team team = teamReader.read(teamId);
        teamWriter.requestJoinTeam(teamId, memberId);
    }

    /**
     * 팀 가입 요청 승인
     * 관리자 또는 주문자만 승인 가능하다
     * 팀의 가입한 사용자의 권한은 뷰어이다.
     *
     * @param teamApplyId 가입 요청 Id
     * @param role        승인하는 사람의 역할
     */
    public void acceptTeamJoinRequest(Long teamApplyId, TeamMemberRole role) {
        teamValidator.canUpdateTeamJoinRequest(role);
        // 요청 status 변경 & 팀에 viewer로 가입
        TeamApplyInfo applyInfo = teamReader.readTeamApplyInfo(teamApplyId);
        teamWriter.acceptJoinRequest(applyInfo);
    }

    /**
     * 팀 가입 요청 거절
     * 관리자 또는 주문자만 거절 가능하다
     *
     * @param teamApplyId 가입 요청 Id
     * @param role        거절하는 사람의 역할
     */
    public void rejectTeamJoinRequest(Long teamApplyId, TeamMemberRole role) {
        teamValidator.canUpdateTeamJoinRequest(role);
        TeamApplyInfo applyInfo = teamReader.readTeamApplyInfo(teamApplyId);
        teamWriter.rejectJoinRequest(teamApplyId);
    }

    /**
     * 팀의 검색 허용 여부를 수정
     * 관리자만 수정가능하다.
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

//    public TeamMemberListResponse searchParticipants(Long teamId, String name) {
//        List<QueryTeamMember> mentions = participantRepository.findMentionsByNamePrefix(name, teamId);
//
//        //mentions가 null인 경우, emptyList를 반환하도록 설정
//        return teamMapper.toTeamMemberListResponse(
//                Objects.requireNonNullElse(mentions, Collections.emptyList())
//        );
//    }
}
