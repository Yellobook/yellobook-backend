package com.yellobook.domains.inform.service;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.common.vo.TeamMemberVO;
import com.yellobook.domains.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.auth.security.oauth2.dto.OAuth2UserDTO;
import com.yellobook.domains.auth.service.RedisTeamService;
import com.yellobook.domains.inform.dto.request.CreateInformCommentRequest;
import com.yellobook.domains.inform.dto.request.CreateInformRequest;
import com.yellobook.domains.inform.dto.response.CreateInformCommentResponse;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformComment;
import com.yellobook.domains.inform.entity.InformMention;
import com.yellobook.domains.inform.mapper.CommentMapper;
import com.yellobook.domains.inform.mapper.InformMapper;
import com.yellobook.domains.inform.repository.InformCommentRepository;
import com.yellobook.domains.inform.repository.InformMentionRepository;
import com.yellobook.domains.inform.repository.InformRepository;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.member.repository.MemberRepository;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.repository.ParticipantRepository;
import com.yellobook.domains.team.repository.TeamRepository;
import com.yellobook.error.code.InformErrorCode;
import com.yellobook.error.code.TeamErrorCode;
import com.yellobook.error.exception.CustomException;
import fixture.InformFixture;
import fixture.MemberFixture;
import fixture.TeamFixture;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InformCommandServiceTest {

    @Mock
    private InformRepository informRepository;
    @Mock
    private InformCommentRepository informCommentRepository;
    @Mock
    private InformMentionRepository informMentionRepository;
    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private InformMapper informMapper;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private RedisTeamService redisService;

    @InjectMocks
    private InformCommandService informCommandService;

    CustomOAuth2User customOAuth2User;
    Member member;
    Team team;
    Participant participant;
    Inform inform;


    @BeforeEach
    public void setUp() {
        member = MemberFixture.createMember();
        team = TeamFixture.createTeam();
        participant = TeamFixture.createParticipant(team, member, MemberTeamRole.ADMIN);
        inform = InformFixture.createInform(team, member);

        OAuth2UserDTO oAuth2UserDTO = OAuth2UserDTO.from(member);
        customOAuth2User = new CustomOAuth2User(oAuth2UserDTO);
    }

    @Nested
    @DisplayName("createInform 메소드는")
    class Describe_createInform{

        @Nested
        @DisplayName("Redis에서 조회한 participant가 존재하지 않는 경우")
        class Context_Not_Exist_Participant_Searching_From_Redis{

            CreateInformRequest request;
            CustomException exception;

            @BeforeEach
            void setUp() {
                request = new CreateInformRequest("test", "test", List.of(), LocalDate.now());
                when(redisService.getCurrentTeamMember(member.getId()))
                        .thenThrow(new CustomException(TeamErrorCode.USER_NOT_JOINED_ANY_TEAM));

                exception = assertThrows(CustomException.class, () -> {
                    informCommandService.createInform(member.getId(), request);
                });
            }

            @Test
            @DisplayName("USER_NOT_JOINED_ANY_TEAM 에러를 반환한다.")
            void it_returns_team_not_found(){
                assertEquals(TeamErrorCode.USER_NOT_JOINED_ANY_TEAM, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("Redis에서 조회한 participant가 존재하는 경우")
        class Context_Exist_Participant_Searching_From_Redis{

            CreateInformRequest request;
            Member member2;

            @BeforeEach
            void setUp() {
                request = new CreateInformRequest("test", "test", List.of(2L), LocalDate.now());

                TeamMemberVO teamMemberVO = mock(TeamMemberVO.class);
                when(redisService.getCurrentTeamMember(member.getId())).thenReturn(teamMemberVO);
                when(teamMemberVO.getTeamId()).thenReturn(team.getId());

                member2 = Member.builder().memberId(2L).build();
                when(memberRepository.findById(2L)).thenReturn(Optional.of(member2));
                when(participantRepository.findByTeamIdAndMemberId(team.getId(), member.getId())).thenReturn(Optional.of(participant));
                when(informMapper.toInform(request, member, team)).thenReturn(inform);

                informCommandService.createInform(member.getId(), request);
            }

            @Test
            @DisplayName("inform을 생성하고 저장한다.")
            void it_returns_saved_inform(){
                verify(informRepository).save(any(Inform.class));
            }

            @Test
            @DisplayName("멘션된 팀원이 있으면 InformMention에 저장한다.")
            void it_returns_saved_mentioned_to_inform_mention(){
                verify(informMapper).toInformMention(inform, member2);
                verify(informMentionRepository).saveAll(anyList());
            }
        }
    }

    @Nested
    @DisplayName("deleteInform 메소드는")
    class Describe_deleteInform{

        @Nested
        @DisplayName("작성자의 삭제 요청이 아닌 경우")
        class Context_Not_Request_From_Writer{

            CustomException exception;
            Member writerMember;

            @BeforeEach
            void setUp() {
                writerMember = mock(Member.class);
                when(writerMember.getId()).thenReturn(999L);

                Inform inform = mock(Inform.class);
                when(inform.getMember()).thenReturn(writerMember);
                when(informRepository.findById(anyLong())).thenReturn(Optional.of(inform));

                exception = assertThrows(CustomException.class, () -> {
                    informCommandService.deleteInform(1L, member.getId());  // 테스트할 메서드 호출
                });
            }

            @Test
            @DisplayName("INFORM_MEMBER_NOT_MATCH 에러를 반환한다.")
            void it_returns_inform_member_not_match(){
                assertEquals(InformErrorCode.INFORM_MEMBER_NOT_MATCH, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("작성자의 삭제 요청인 경우")
        class Context_Request_From_Writer{

            Member writer;
            Inform inform;

            @BeforeEach
            void setUp() {
                writer = mock(Member.class);
                when(writer.getId()).thenReturn(1L);

                inform = mock(Inform.class);
                when(inform.getId()).thenReturn(2L);
                when(inform.getMember()).thenReturn(writer);
                when(informRepository.findById(anyLong())).thenReturn(Optional.of(inform));

                doNothing().when(informRepository).deleteById(anyLong());
            }

            @Test
            @DisplayName("inform을 삭제한다.")
            void it_returns_deleted_inform(){
                informCommandService.deleteInform(inform.getId(), writer.getId());

                verify(informRepository).deleteById(anyLong());
            }
        }
    }
}
