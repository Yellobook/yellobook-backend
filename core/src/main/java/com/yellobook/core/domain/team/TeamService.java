package com.yellobook.core.domain.team;

import com.yellobook.core.domain.team.mapper.ParticipantMapper;
import com.yellobook.core.domain.team.mapper.TeamMapper;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final TeamMapper teamMapper;
    private final ParticipantMapper participantMapper;
    private final TeamService teamService;

    public CreateTeamResponse createTeam(CreateTeamRequest request, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("Member {} not found.", memberId);
                    return new CustomException(TeamErrorCode.MEMBER_NOT_FOUND);
                });
        if (teamRepository.findByName(request.name())
                .isPresent()) {
            log.warn("Team {} already exists.", request.name());
            throw new CustomException(TeamErrorCode.EXIST_TEAM_NAME);
        } else {
            Team newTeam = teamMapper.toTeam(request);
            teamRepository.save(newTeam);
            log.info("New team created: {}", newTeam.getId());
            Participant founder = participantMapper.toParticipant(request.role(), newTeam, member);
            participantRepository.save(founder);
            log.info("Participant added: Member ID = {}, Team ID = {}", member.getId(), newTeam.getId());

            teamService.setMemberCurrentTeam(member.getId(), newTeam.getId(), request.role()
                    .name());

            return teamMapper.toCreateTeamResponse(newTeam);
        }
    }

    public void leaveTeam(Long teamId, Long memberId) {
        Participant participant = participantRepository.findByTeamIdAndMemberId(teamId, memberId)
                .orElseThrow(() -> {
                    log.warn("Participant not found for Member ID = {} and Team ID = {}", memberId, teamId);
                    return new CustomException(TeamErrorCode.USER_NOT_IN_THAT_TEAM);
                });

        participantRepository.delete(participant);
        log.info("Participant (Member ID = {}, Team ID = {}) is deleted", memberId, teamId);

        participantRepository.findFirstByMemberIdOrderByCreatedAtAsc(memberId)
                .ifPresent(tempParticipant -> {
                    teamService.setMemberCurrentTeam(
                            memberId,
                            tempParticipant.getTeam()
                                    .getId(),
                            tempParticipant.getTeamMemberRole()
                                    .name()
                    );
                });
    }

    public JoinTeamResponse joinTeam(Long memberId, String code) {

        InvitationResponse invitationData = teamService.getInvitationInfo(code);
        Long teamId = invitationData.getTeamId();
        TeamMemberRole role = invitationData.getRole();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("Member not found: {}", memberId);
                    return new CustomException(TeamErrorCode.MEMBER_NOT_FOUND);
                });

        //팀이 유효한가?
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));

        if (participantRepository.findByTeamIdAndMemberId(teamId, memberId)
                .isPresent()) {
            log.warn("Member {} already on the team {}", memberId, teamId);
            throw new CustomException(TeamErrorCode.MEMBER_ALREADY_EXIST);
        }
        // ADMIN이 있는가?
        if (role == TeamMemberRole.ADMIN) {
            participantRepository.findByTeamIdAndTeamMemberRole(teamId, TeamMemberRole.ADMIN)
                    .ifPresent(
                            admin -> {
                                log.warn("Admin already exists in team {}", teamId);
                                throw new CustomException(TeamErrorCode.ADMIN_EXISTS);
                            });
        }
        participantRepository.save(participantMapper.toParticipant(role, team, member));
        log.info("Participant added: Team ID = {}, Role = {}, Member ID = {}", teamId, role, memberId);

        teamService.setMemberCurrentTeam(memberId, teamId, role.name());

        return teamMapper.toJoinTeamResponse(team);
    }

    public InvitationCodeResponse makeInvitationCode(
            Long teamId,
            InvitationCodeRequest request,
            Long memberId
    ) {
        TeamMemberRole role = request.role();

        Participant participant = participantRepository.findByTeamIdAndMemberId(teamId, memberId)
                .orElseThrow(() -> {
                    log.warn("Cannot make invitation: Member ID = {}, Team ID = {}", memberId, teamId);
                    return new CustomException(TeamErrorCode.USER_NOT_IN_THAT_TEAM);
                });

        validateInvitationPermission(participant, role, teamId);
        String invitationUrl = teamService.generateInvitationUrl(teamId, role);

        return teamMapper.toInvitationCodeResponse(invitationUrl);
    }

    private void validateInvitationPermission(Participant participant, TeamMemberRole requestedRole, Long teamId) {
        TeamMemberRole currentRole = participant.getTeamMemberRole();

        if (currentRole == TeamMemberRole.VIEWER) {
            log.warn("VIEWER cannot invite: Participant ID = {}, Team ID = {}", participant.getId(), teamId);
            throw new CustomException(TeamErrorCode.VIEWER_CANNOT_INVITE);
        }

        if (requestedRole == TeamMemberRole.ADMIN) {
            if (currentRole != TeamMemberRole.ADMIN) {
                participantRepository.findByTeamIdAndTeamMemberRole(teamId, TeamMemberRole.ADMIN)
                        .ifPresent(admin -> {
                            log.warn("ADMIN already exists: Team ID = {}", teamId);
                            throw new CustomException(TeamErrorCode.ADMIN_EXISTS);
                        });
            }
        }
    }

    public GetTeamResponse findByTeamId(Long teamId, Long memberId) {
        //팀 id를 가지고 팀에 대한 정보 가져오기
        Participant participant = participantRepository.findByTeamIdAndMemberId(teamId, memberId)
                .orElseThrow(() -> {
                    log.warn("Cannot get team: Member ID = {}, Team ID = {}", memberId, teamId);
                    return new CustomException(TeamErrorCode.USER_NOT_IN_THAT_TEAM);
                });

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));

        teamService.setMemberCurrentTeam(memberId, teamId, participant.getTeamMemberRole()
                .name());

        return teamMapper.toGetTeamResponse(team);
    }

    public TeamMemberListResponse findTeamMembers(Long teamId) {
        List<QueryTeamMember> members = teamRepository.findTeamMembers(teamId);

        //members 가 null인 경우, emptyList를 반환하도록 설정
        return teamMapper.toTeamMemberListResponse(
                Objects.requireNonNullElse(members, Collections.emptyList())
        );
    }


    public boolean existsByTeamId(Long teamId) {
        return teamRepository.existsById(teamId);
    }

    public TeamMemberListResponse searchParticipants(Long teamId, String name) {
        List<QueryTeamMember> mentions = participantRepository.findMentionsByNamePrefix(name, teamId);

        //mentions가 null인 경우, emptyList를 반환하도록 설정
        return teamMapper.toTeamMemberListResponse(
                Objects.requireNonNullElse(mentions, Collections.emptyList())
        );
    }

}
