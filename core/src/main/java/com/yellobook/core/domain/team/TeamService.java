package com.yellobook.core.domain.team;

import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.team.dto.CreateInvitationCodeCommand;
import com.yellobook.core.domain.team.dto.CreateTeamCommand;
import com.yellobook.core.domain.team.dto.InvitationCodeInfo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    private final TeamReader teamReader;
    private final TeamWriter teamWriter;
    private final TeamCachedManager teamCachedManager;

    public TeamService(TeamReader teamReader, TeamWriter teamWriter, TeamCachedManager teamCachedManager) {
        this.teamReader = teamReader;
        this.teamWriter = teamWriter;
        this.teamCachedManager = teamCachedManager;
    }

    /*
    팀 생성
     */
    @Transactional
    public Long create(CreateTeamCommand command, Member member) {
        teamReader.isPresent(command.name());
        Long teamId = teamWriter.create(command.name(), command.phoneNumber(), command.address());
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

    /*
    팀 참가
     */
    @Transactional
    public void join(String key, Member member) {
        InvitationCodeInfo info = teamCachedManager.read(key);
        teamReader.validateNotParticipant(info.teamId(), member);
        teamWriter.join(info.teamId(), member, info.role());
        teamCachedManager.setMemberCurrentTeam(info.teamId(), member.memberId(), info.role());
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

//    public TeamMemberListResponse searchParticipants(Long teamId, String name) {
//        List<QueryTeamMember> mentions = participantRepository.findMentionsByNamePrefix(name, teamId);
//
//        //mentions가 null인 경우, emptyList를 반환하도록 설정
//        return teamMapper.toTeamMemberListResponse(
//                Objects.requireNonNullElse(mentions, Collections.emptyList())
//        );
//    }
}
