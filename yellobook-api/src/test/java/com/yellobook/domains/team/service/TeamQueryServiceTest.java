package com.yellobook.domains.team.service;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.auth.security.oauth2.dto.OAuth2UserDTO;
import com.yellobook.domains.auth.service.RedisTeamService;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.dto.query.QueryTeamMember;
import com.yellobook.domains.team.dto.request.InvitationCodeRequest;
import com.yellobook.domains.team.dto.response.GetTeamResponse;
import com.yellobook.domains.team.dto.response.InvitationCodeResponse;
import com.yellobook.domains.team.dto.response.TeamMemberListResponse;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.mapper.TeamMapper;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.domains.team.repository.TeamRepository;
import com.yellobook.error.code.TeamErrorCode;
import com.yellobook.error.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamQueryServiceTest {
    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private RedisTeamService redisService;

    @Mock
    private TeamMapper teamMapper;

    @InjectMocks
    private TeamQueryService teamQueryService;

    private CustomOAuth2User customOAuth2User;

    private Member member;
    private Team team;
    private Participant participant;


    @BeforeEach
    void setUp() {
        member = createMember(1L);
        team = createTeam(1L);
        participant = createParticipant(1L, team, member, MemberTeamRole.ORDERER);

        OAuth2UserDTO oauth2UserDTO = OAuth2UserDTO.from(member);
        customOAuth2User = new CustomOAuth2User(oauth2UserDTO);
    }

    @Nested
    @DisplayName("makeInvitationCode 메소드는")
    class Describe_makeInvitationCode {

        @Nested
        @DisplayName("멤버가 팀에 속하지 않은 경우")
        class Context_Member_Not_Belong_Team{

            @Test
            @DisplayName("USER_NOT_IN_THAT_TEAM 에러를 반환한다.")
            void it_returns_cannot_invite(){
                //given
                InvitationCodeRequest request = new InvitationCodeRequest(MemberTeamRole.ORDERER);

                when(participantRepository.findByTeamIdAndMemberId(team.getId(), member.getId())).thenReturn(Optional.empty());

                //when & then
                CustomException exception = assertThrows(
                        CustomException.class, () -> teamQueryService.makeInvitationCode(team.getId(), request, customOAuth2User));

                assertEquals(TeamErrorCode.USER_NOT_IN_THAT_TEAM, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("ADMIN이 이미 있을 때, ADMIN을 초대하는 경우")
        class Context_Admin_Already_Exists{

            @Test
            @DisplayName("ADMIN_EXISTS 에러를 반환한다.")
            void it_returns_admin_exists(){
                //given
                Long teamId = 1L;
                Long memberId = 1L;
                Long memberId2 = 2L;
                Member member2 = createMember(memberId2);
                Participant adminPar = createParticipant(2L, team, member2, MemberTeamRole.ADMIN);
                InvitationCodeRequest request = new InvitationCodeRequest(MemberTeamRole.ADMIN);

                when(participantRepository.findByTeamIdAndMemberId(teamId, memberId))
                        .thenReturn(Optional.of(participant));
                when(participantRepository.findByTeamIdAndRole(teamId, MemberTeamRole.ADMIN))
                        .thenReturn(Optional.of(adminPar));

                CustomException exception = assertThrows(
                        CustomException.class, () -> teamQueryService.makeInvitationCode(teamId, request, customOAuth2User));

                assertEquals(TeamErrorCode.ADMIN_EXISTS, exception.getErrorCode());
                verify(participantRepository).findByTeamIdAndRole(teamId, MemberTeamRole.ADMIN);
            }
        }

        @Nested
        @DisplayName("문제가 없는 경우")
        class Context_No_Problem{

            @Test
            @DisplayName("초대 코드를 발급한다.")
            void it_returns_invitation_code(){
                //given
                InvitationCodeRequest request = new InvitationCodeRequest(MemberTeamRole.ORDERER);

                when(participantRepository.findByTeamIdAndMemberId(team.getId(), customOAuth2User.getMemberId()))
                        .thenReturn(Optional.of(participant)); // 참가자 정보
                when(redisService.generateInvitationUrl(team.getId(), request.role()))
                        .thenReturn("http://invitation-url"); // 초대 URL 생성
                when(teamMapper.toInvitationCodeResponse(anyString()))
                        .thenReturn(new InvitationCodeResponse("http://invitation-url")); // 응답 매핑

                // When
                InvitationCodeResponse response = teamQueryService.makeInvitationCode(team.getId(), request, customOAuth2User);

                // Then
                assertNotNull(response);
                assertEquals("http://invitation-url", response.inviteUrl());
                verify(redisService).generateInvitationUrl(team.getId(), request.role());
            }
        }
    }

    private Member createMember(Long memberId){
        return Member.builder().memberId(memberId).build();
    }

    private Team createTeam(Long teamId){
        Team team = Team.builder().name("test").phoneNumber("010").address("seoul").build();
        try {
            Field idField = Team.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(team, teamId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set team ID", e);
        }

        return team;
    }

    private Participant createParticipant(Long participantId, Team team, Member member, MemberTeamRole role){
        Participant participant = Participant.builder().team(team).member(member).role(role).build();
        try {
            Field idField = Participant.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(participant, participantId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set participant ID", e);
        }

        return participant;
    }
}
