package com.yellobook.domains.member.mapper;

import static com.yellobook.domains.member.dto.response.ProfileResponse.ParticipantInfo;
import static fixture.MemberFixture.createMember;
import static org.assertj.core.api.Assertions.assertThat;

import com.yellobook.common.enums.TeamMemberRole;
import com.yellobook.domains.member.dto.response.ProfileResponse;
import com.yellobook.domains.member.dto.response.TermAllowanceResponse;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.dto.query.QueryMemberJoinTeam;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

@DisplayName("MemberMapper Unit Test")
class MemberMapperTest {

    private final MemberMapper mapper = Mappers.getMapper(MemberMapper.class);

    @Nested
    @DisplayName("toParticipantInfo 메서드는")
    class Describe_toParticipantInfo {
        @Nested
        @DisplayName("QueryMemberJoinTeam 을 받아")
        class Context_with_queryMemberJoinTeam_is_mapped {
            QueryMemberJoinTeam source;

            @BeforeEach
            void setUpContext() {
                source = QueryMemberJoinTeam.builder()
                        .teamId(1L)
                        .teamName("팀1")
                        .role(TeamMemberRole.ADMIN)
                        .build();
            }

            @Test
            @DisplayName("ParticipantInfo로 변환한다.")
            void it_returns_participantInfo() {
                ParticipantInfo target = mapper.toParticipantInfo(source);
                assertThat(target).isNotNull();
                assertThat(target.teamId()).isEqualTo(source.teamId());
                assertThat(target.teamName()).isEqualTo(source.teamName());
                assertThat(target.role()).isEqualTo(source.role()
                        .getDescription());
            }
        }
    }

    @Nested
    @DisplayName("toProfileResponse 메서드는")
    class Describe_toProfileResponse {
        @Nested
        @DisplayName("Member 와 List<QueryMemberJoinTeam> 을 받아")
        class Context_with_member_and_participantInfos_are_mapped {
            Member member;
            List<QueryMemberJoinTeam> memberJoinTeams;

            @BeforeEach
            void setUpContext() {
                member = createMember();
                memberJoinTeams = List.of(
                        QueryMemberJoinTeam.builder()
                                .teamId(1L)
                                .teamName("팀1")
                                .role(TeamMemberRole.ADMIN)
                                .build(),
                        QueryMemberJoinTeam.builder()
                                .teamId(2L)
                                .teamName("팀2")
                                .role(TeamMemberRole.ADMIN)
                                .build()
                );
            }

            @Test
            @DisplayName("ProfileResponse로 변환한다.")
            void it_returns_ProfileResponse() {
                ProfileResponse target = mapper.toProfileResponse(member, memberJoinTeams);

                assertThat(target).isNotNull();
                assertThat(target.memberId()).isEqualTo(member.getId());
                assertThat(target.teams()).hasSize(memberJoinTeams.size());

                IntStream.range(0, memberJoinTeams.size())
                        .forEach(i -> {
                            var expected = memberJoinTeams.get(i);
                            var actual = target.teams()
                                    .get(i);

                            assertThat(actual.teamName()).isEqualTo(expected.teamName());
                            assertThat(actual.teamId()).isEqualTo(expected.teamId());
                            assertThat(actual.role()).isEqualTo(expected.role()
                                    .getDescription());
                        });
            }
        }
    }

    @Nested
    @DisplayName("toAllowanceResponse 메서드는")
    class Describe_toAllowanceResponse {

        @Nested
        @DisplayName("Boolean 값을 받아")
        class Context_with_boolean_input {
            Boolean allowance;

            @BeforeEach
            void setUpContext() {
                allowance = true;
            }

            @Test
            @DisplayName("TermAllowanceResponse로 변환한다.")
            void it_returns_TermAllowanceResponse() {
                TermAllowanceResponse response = mapper.toAllowanceResponse(allowance);

                assertThat(response).isNotNull();
                assertThat(response.allowance()).isTrue();
            }
        }
    }
}