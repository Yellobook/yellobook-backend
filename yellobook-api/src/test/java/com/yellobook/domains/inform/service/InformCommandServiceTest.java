package com.yellobook.domains.inform.service;

import static fixture.InformFixture.createInform;
import static fixture.MemberFixture.createMember;
import static fixture.TeamFixture.createParticipant;
import static fixture.TeamFixture.createTeam;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yellobook.common.enums.TeamMemberRole;
import com.yellobook.common.vo.TeamMemberVO;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Nested
    @DisplayName("createInform 메소드는")
    class Describe_createInform {

        @Nested
        @DisplayName("Redis에서 조회한 participant가 존재하지 않는 경우")
        class Context_Not_Exist_Participant_Searching_From_Redis {

            Member member;
            CreateInformRequest request;
            CustomException exception;
            Long memberId;

            @BeforeEach
            void setUp() {
                member = createMember();
                memberId = member.getId();
                request = new CreateInformRequest("test", "test", List.of(), LocalDate.now());
                when(redisService.getCurrentTeamMember(memberId))
                        .thenThrow(new CustomException(TeamErrorCode.USER_NOT_JOINED_ANY_TEAM));

                exception = assertThrows(CustomException.class, () -> {
                    informCommandService.createInform(memberId, request);
                });
            }

            @Test
            @DisplayName("USER_NOT_JOINED_ANY_TEAM 에러를 반환한다.")
            void it_returns_team_not_found() {
                assertEquals(TeamErrorCode.USER_NOT_JOINED_ANY_TEAM, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("Redis에서 조회한 participant가 존재하는 경우")
        class Context_Exist_Participant_Searching_From_Redis {

            Member member;
            Team team;
            Participant participant;
            Inform inform;
            CreateInformRequest request;
            Member member2;

            @BeforeEach
            void setUp() {
                member = mock(Member.class);
                team = createTeam();
                participant = createParticipant(team, member, TeamMemberRole.ADMIN);
                inform = createInform(team, member);

                request = new CreateInformRequest("test", "test", List.of(2L), LocalDate.now());

                TeamMemberVO teamMemberVO = mock(TeamMemberVO.class);
                when(redisService.getCurrentTeamMember(member.getId())).thenReturn(teamMemberVO);
                when(teamMemberVO.getTeamId()).thenReturn(team.getId());

                member2 = Member.builder()
                        .memberId(2L)
                        .build();
                when(memberRepository.findById(2L)).thenReturn(Optional.of(member2));
                when(participantRepository.findByTeamIdAndMemberId(team.getId(), member.getId())).thenReturn(
                        Optional.of(participant));
                when(informMapper.toInform(request, member, team)).thenReturn(inform);

                informCommandService.createInform(member.getId(), request);
            }

            @Test
            @DisplayName("inform을 생성하고 저장한다.")
            void it_returns_saved_inform() {
                verify(informRepository).save(any(Inform.class));
            }

            @Test
            @DisplayName("멘션된 팀원이 있으면 InformMention에 저장한다.")
            void it_returns_saved_mentioned_to_inform_mention() {
                verify(informMapper).toInformMention(inform, member2);
                verify(informMentionRepository).saveAll(anyList());
            }
        }
    }

    @Nested
    @DisplayName("deleteInform 메소드는")
    class Describe_deleteInform {

        @Nested
        @DisplayName("작성자의 삭제 요청이 아닌 경우")
        class Context_Not_Request_From_Writer {

            CustomException exception;
            Member writerMember;
            Member member;
            Long memberId;

            @BeforeEach
            void setUp() {
                member = mock(Member.class);
                memberId = member.getId();
                writerMember = mock(Member.class);
                when(writerMember.getId()).thenReturn(999L);

                Inform inform = mock(Inform.class);
                when(inform.getMember()).thenReturn(writerMember);
                when(informRepository.findById(anyLong())).thenReturn(Optional.of(inform));

                exception = assertThrows(CustomException.class, () -> {
                    informCommandService.deleteInform(1L, memberId);  // 테스트할 메서드 호출
                });
            }

            @Test
            @DisplayName("INFORM_MEMBER_NOT_MATCH 에러를 반환한다.")
            void it_returns_inform_member_not_match() {
                assertEquals(InformErrorCode.INFORM_MEMBER_NOT_MATCH, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("작성자의 삭제 요청인 경우")
        class Context_Request_From_Writer {

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

                doNothing().when(informRepository)
                        .deleteById(anyLong());
            }

            @Test
            @DisplayName("inform을 삭제한다.")
            void it_returns_deleted_inform() {
                informCommandService.deleteInform(inform.getId(), writer.getId());

                verify(informRepository).deleteById(anyLong());
            }
        }
    }

    @Nested
    @DisplayName("addComment 메소드는")
    class Describe_addComment {

        @Nested
        @DisplayName("언급되지 않은 사용자의 요청인 경우")
        class Context_Request_From_Not_Mentioned {

            CreateInformCommentRequest request;
            CustomException exception;
            Member notMentioned;
            Inform wrotenInform;
            Member member;
            Long writenInformId;
            Long notMentionedId;

            @BeforeEach
            void setUp() {
                member = mock(Member.class);
                request = new CreateInformCommentRequest("test");
                notMentioned = Member.builder()
                        .memberId(99L)
                        .build();
                wrotenInform = mock(Inform.class);
                when(wrotenInform.getMember()).thenReturn(member);
                when(wrotenInform.getId()).thenReturn(2L);

                writenInformId = wrotenInform.getId();
                notMentionedId = notMentioned.getId();

                when(informRepository.findById(wrotenInform.getId())).thenReturn(Optional.of(wrotenInform));
                when(informMentionRepository.findAllByInformId(writenInformId)).thenReturn(List.of(
                        InformMention.builder()
                                .inform(wrotenInform)
                                .member(mock(Member.class))
                                .build()
                ));
                exception = assertThrows(CustomException.class, () -> {
                    informCommandService.addComment(writenInformId, notMentionedId, request);
                });
            }

            @Test
            @DisplayName("NOT_MENTIONED 에러를 반환한다.")
            void it_returns_not_mentioned() {
                assertEquals(InformErrorCode.NOT_MENTIONED, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("언급된 사용자, 혹은 작성자의 요청인 경우")
        class Context_Request_From_Mentioned_Or_Writer {

            CreateInformCommentRequest request;
            InformComment comment;
            Member mentionedMember;
            Inform inform;

            @BeforeEach
            void setUp() {
                request = new CreateInformCommentRequest("test");
                mentionedMember = mock(Member.class);
                inform = mock(Inform.class);

                when(mentionedMember.getId()).thenReturn(1L);

                when(memberRepository.findById(mentionedMember.getId())).thenReturn(Optional.of(mentionedMember));
                when(inform.getMember()).thenReturn(mentionedMember);
                comment = InformComment.builder()
                        .inform(inform)
                        .member(mentionedMember)
                        .content("test")
                        .build();
                when(informRepository.findById(inform.getId())).thenReturn(Optional.of(inform));
                when(informCommentRepository.save(any(InformComment.class))).thenReturn(comment);
                when(commentMapper.toCreateInformCommentResponse(any(InformComment.class)))
                        .thenReturn(new CreateInformCommentResponse(1L, comment.getCreatedAt()));

                when(informMentionRepository.findAllByInformId(inform.getId())).thenReturn(List.of(
                        new InformMention(inform, mentionedMember)
                ));
            }

            @Test
            @DisplayName("댓글을 저장하고 성공 응답을 반환한다.")
            void it_returns_saved_comment_and_response() {
                CreateInformCommentResponse response = informCommandService.addComment(inform.getId(),
                        mentionedMember.getId(), request);

                assertNotNull(response);
                verify(informCommentRepository).save(any(InformComment.class));
            }
        }
    }

    @Nested
    @DisplayName("increaseViewCount 메소드는")
    class Describe_IncreaseViewCount {

        @Nested
        @DisplayName("inform이 존재하지 않은 경우")
        class Context_Not_Exist_inform {

            Long notExistInformId;
            CustomException exception;
            TeamMemberVO teamMember;

            @BeforeEach
            void setUp() {
                notExistInformId = 99L;
                teamMember = mock(TeamMemberVO.class);

                when(informRepository.findById(notExistInformId)).thenReturn(Optional.empty());
                exception = assertThrows(CustomException.class, () -> {
                    informCommandService.increaseViewCount(notExistInformId, teamMember);
                });
            }

            @Test
            @DisplayName("INFORM_NOT_FOUND 에러를 반환한다.")
            void it_returns_inform_not_found() {
                assertEquals(InformErrorCode.INFORM_NOT_FOUND, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("본인이 작성하지 않고 멘션되지 않은 경우")
        class Context_Not_Author_And_Not_Mentioned {

            Inform inform;
            Long informId;
            Team team;
            Member Author;
            Member nonMentionedMember;
            TeamMemberVO teamMember;
            CustomException exception;

            @BeforeEach
            void setUp() {
                team = mock(Team.class);
                Author = mock(Member.class);
                nonMentionedMember = Member.builder()
                        .memberId(99L)
                        .build();
                inform = mock(Inform.class);
                when(informRepository.findById(informId)).thenReturn(Optional.of(inform));
                when(inform.getMember()).thenReturn(Author);

                teamMember = TeamMemberVO.of(nonMentionedMember.getId(), team.getId(), TeamMemberRole.ADMIN);

                exception = assertThrows(CustomException.class, () -> {
                    informCommandService.increaseViewCount(informId, teamMember);
                });
            }

            @Test
            @DisplayName("NOT_MENTIONED 에러를 반환한다.")
            void it_returns_not_mentioned() {
                assertEquals(InformErrorCode.NOT_MENTIONED, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("작성자의 요청일 경우")
        class Context_Author {

            Inform inform;
            Member author;
            TeamMemberVO teamMember;

            @BeforeEach
            void setUp() {
                inform = mock(Inform.class);
                author = mock(Member.class);

                when(inform.getId()).thenReturn(1L);
                when(informRepository.findById(inform.getId())).thenReturn(Optional.of(inform));
                when(inform.getMember()).thenReturn(author);
                teamMember = TeamMemberVO.of(author.getId(), 1L, TeamMemberRole.ADMIN);
                doNothing().when(inform)
                        .updateView();
            }

            @Test
            @DisplayName("조회수를 증가시킨다.")
            void it_returns_update_view() {
                informCommandService.increaseViewCount(inform.getId(), teamMember);
                verify(inform).updateView();
            }
        }

        @Nested
        @DisplayName("언급된 사용자의 요청일 경우")
        class Context_Mentioned {

            Inform inform;
            Member mentionedMember;
            Member author;
            TeamMemberVO teamMember;

            @BeforeEach
            void setUp() {
                inform = mock(Inform.class);
                mentionedMember = mock(Member.class);
                author = mock(Member.class);

                when(inform.getMember()).thenReturn(author);
                when(inform.getId()).thenReturn(1L);
                when(informRepository.findById(inform.getId())).thenReturn(Optional.of(inform));

                teamMember = TeamMemberVO.of(mentionedMember.getId(), 1L, TeamMemberRole.ADMIN);

                doNothing().when(inform)
                        .updateView();
            }

            @Test
            @DisplayName("조회수를 증가시킨다.")
            void it_returns_update_view() {
                informCommandService.increaseViewCount(inform.getId(), teamMember);
                verify(inform).updateView();
            }
        }
    }
}
