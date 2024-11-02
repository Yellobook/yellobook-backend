package com.yellobook.domains.inform.mapper;

import static fixture.InformFixture.createInform;
import static fixture.InformFixture.createInformComment;
import static fixture.MemberFixture.createMember;
import static fixture.TeamFixture.createTeam;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.yellobook.domains.inform.dto.request.CreateInformRequest;
import com.yellobook.domains.inform.dto.response.CreateInformCommentResponse;
import com.yellobook.domains.inform.dto.response.CreateInformResponse;
import com.yellobook.domains.inform.dto.response.GetInformResponse;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformComment;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Team;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

@DisplayName("InformMapper Unit Test")
class InformMapperTest {

    private final InformMapper mapper = Mappers.getMapper(InformMapper.class);

    @Nested
    @DisplayName("toInform 메서드는")
    class Describe_toInform {

        @Nested
        @DisplayName("CreateInformRequest, Member, Team을 받아")
        class Context_with_createInformRequest_member_team {
            CreateInformRequest request;
            Member member;
            Team team;

            @BeforeEach
            void setUpContext() {
                request = CreateInformRequest.builder()
                        .title("제목")
                        .memo("내용")
                        .date(LocalDate.of(2024, 11, 2))
                        .build();

                member = createMember();
                team = createTeam("팀1");
            }

            @Test
            @DisplayName("Inform을 반환한다.")
            void it_returns_Inform() {
                Inform target = mapper.toInform(request, member, team);

                assertThat(target).isNotNull();
                assertThat(target.getContent()).isEqualTo(request.memo());
                assertThat(target.getMember()
                        .getId()).isEqualTo(member.getId());
                assertThat(target.getTeam()
                        .getId()).isEqualTo(team.getId());
            }
        }
    }

    @Nested
    @DisplayName("toCreateInformResponse 메서드는")
    class Describe_toCreateInformResponse {

        @Nested
        @DisplayName("Inform을 받아")
        class Context_with_inform {
            Inform inform;

            @BeforeEach
            void setUpContext() {
                Team team = createTeam("팀1");
                Member member = createMember();
                inform = createInform(team, member);
            }

            @Test
            @DisplayName("CreateInformResponse을 반환한다.")
            void it_returns_CreateInformResponse() {
                CreateInformResponse target = mapper.toCreateInformResponse(inform);

                assertThat(target).isNotNull();
                assertThat(target.informId()).isEqualTo(inform.getId());
            }
        }
    }

    @Nested
    @DisplayName("toGetInformResponse 메서드는")
    class Describe_toGetInformResponse {

        @Nested
        @DisplayName("Inform과 List<InformComment>, List<Member>을 받아")
        class Context_with_inform_comments_mentions {
            Inform inform;
            List<InformComment> comments;
            List<Member> mentions;

            @BeforeEach
            void setUpContext() {
                Member member1 = createMember();
                Member member2 = createMember();
                Member member3 = createMember();
                Member member4 = createMember();
                Team team = createTeam("팀1");
                inform = createInform(team, member1);
                comments = List.of(
                        createInformComment(inform, member2),
                        createInformComment(inform, member3)
                );
                mentions = List.of(
                        member2,
                        member3,
                        member4
                );
            }

            @Test
            @DisplayName("GetInformResponse를 반환한다.")
            void it_returns_GetInformResponse() {
                GetInformResponse target = mapper.toGetInformResponse(inform, comments, mentions);

                assertThat(target).isNotNull();
                assertThat(target.title()).isEqualTo(inform.getTitle());
                assertThat(target.memo()).isEqualTo(inform.getContent());
                assertThat(target.author()).isEqualTo(inform.getMember()
                        .getNickname());

                assertThat(target.comments()
                        .size()).isEqualTo(comments.size());
                assertThat(target.mentions()
                        .size()).isEqualTo(mentions.size());

                IntStream.range(0, comments.size())
                        .forEach(i -> {
                            var targetComment = target.comments()
                                    .get(i);
                            var sourceComment = comments.get(i);
                            assertThat(targetComment.id()).isEqualTo(sourceComment.getId());
                            assertThat(targetComment.content()).isEqualTo(sourceComment.getContent());
                            assertThat(targetComment.createdAt()).isEqualTo(sourceComment.getCreatedAt());
                            assertThat(targetComment.memberId()).isEqualTo(sourceComment.getMember()
                                    .getId());
                        });

                IntStream.range(0, mentions.size())
                        .forEach(i -> {
                            var targetMention = target.mentions()
                                    .get(i);
                            var sourceMention = mentions.get(i);
                            assertThat(targetMention.memberId()).isEqualTo(sourceMention.getId());
                            assertThat(targetMention.memberNickname()).isEqualTo(sourceMention.getNickname());
                        });
            }
        }
    }

    @Nested
    @DisplayName("toCreateInformCommentResponse 메소드는")
    class Describe_toCreateInformCommentResponse {
        @Nested
        @DisplayName("InformComment 를 받아")
        class Context_with_InformComment {
            InformComment informComment;

            @BeforeEach
            void setUpContext() {
                Member member = createMember();
                Team team = createTeam("팀1");
                Inform inform = createInform(team, member);
                informComment = createInformComment(inform, member);
            }

            @Test
            @DisplayName("Create/InformCommentResponse를 반환한다.")
            void it_returns_CreateInformCommentResponse() {
                CreateInformCommentResponse target = mapper.toCreateInformCommentResponse(informComment);
                assertThat(target).isNotNull();
                assertThat(target.id()).isEqualTo(informComment.getId());
                assertThat(target.createdAt()).isEqualTo(informComment.getCreatedAt());
            }
        }
    }
}
