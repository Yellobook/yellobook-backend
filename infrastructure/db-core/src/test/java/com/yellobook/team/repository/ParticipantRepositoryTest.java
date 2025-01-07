package com.yellobook.domains.team.repository;

import static fixture.MemberFixture.createMember;
import static fixture.TeamFixture.createParticipant;
import static fixture.TeamFixture.createTeam;
import static org.assertj.core.api.Assertions.assertThat;

import com.yellobook.MemberRole;
import com.yellobook.TeamMemberRole;
import com.yellobook.domains.team.dto.query.QueryTeamMember;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.member.Member;
import com.yellobook.support.RepositoryTest;
import com.yellobook.team.Team;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("ParticipantRepository Unit Test")
public class ParticipantRepositoryTest extends RepositoryTest {

    @Autowired
    private ParticipantRepository participantRepository;

    private static final Long NON_EXIST_ID = 99999L;

    @BeforeEach
    public void setUp() {
        resetAutoIncrement();
    }

    @Test
    @DisplayName("참가자 생성")
    public void addParticipant() {
        //given
        Team team = Team.builder()
                .name("team")
                .address("서울 강남구")
                .phoneNumber("010010101")
                .build();
        em.persist(team);

        Member member = new Member(null, "설", "johndoe@gmail.com", "", MemberRole.USER, true);
        em.persist(member);

        Participant participant = createParticipant(team, member, TeamMemberRole.ADMIN);
        //when
        participantRepository.save(participant);

        //then
        Assertions.assertThat(participantRepository.findAll())
                .hasSize(1);
        Assertions.assertThat(participantRepository.findById(participant.getId()))
                .isPresent();
    }

    @Test
    @DisplayName("참가자 삭제")
    public void deleteParticipant() {
        //given
        Team team = Team.builder()
                .name("team")
                .address("서울 강남구")
                .phoneNumber("010010101")
                .build();
        em.persist(team);

        Member member = new Member(null, "설", "johndoe@gmail.com", "", MemberRole.USER, true);
        em.persist(member);

        Participant participant = new Participant(team, member, TeamMemberRole.ADMIN);

        participantRepository.save(participant);

        //when
        participantRepository.deleteById(participant.getId());

        //then
        Assertions.assertThat(participantRepository.findAll())
                .isEmpty();
    }

    //이름을 넣어서 리스트로 받는 조회
    @Nested
    @DisplayName("참가자 조회")
    public class searchParticipant {
        @Test
        @DisplayName("검색어를 통해 리스트로 받는 조회")
        public void searchParticipantByPrefix() {
            // Given
            String prefix = "이";

            Member member1 = em.persist(createMember("이대호"));
            Member member2 = em.persist(createMember("이성원"));
            Member member3 = em.persist(createMember("김복자"));

            Team team = em.persist(createTeam("팀1"));

            em.persist(createParticipant(team, member1, TeamMemberRole.ORDERER));
            em.persist(createParticipant(team, member2, TeamMemberRole.ORDERER));
            em.persist(createParticipant(team, member3, TeamMemberRole.ORDERER));
            // When
            List<QueryTeamMember> result = participantRepository.findMentionsByNamePrefix(prefix, team.getId());

            // Then
            assertThat(result).hasSize(2);
            assertThat(result.get(0)
                    .nickname()).isEqualTo("이대호");
            assertThat(result.get(1)
                    .nickname()).isEqualTo("이성원");
        }

        //팀 id를 통해 모든 참가자를 받는 조회
        @Test
        @DisplayName("팀 id를 통해 모든 참가자 조회")
        public void searchByTeamId() {
            //given
            Member member1 = em.persist(createMember());
            Member member2 = em.persist(createMember());

            Team team1 = em.persist(createTeam("팀1"));
            Team team2 = em.persist(createTeam("팀2"));

            em.persist(createParticipant(team1, member1, TeamMemberRole.ORDERER));
            em.persist(createParticipant(team1, member2, TeamMemberRole.ORDERER));
            //when
            List<Participant> list = participantRepository.findAllByTeamId(team1.getId());
            List<Participant> list2 = participantRepository.findAllByTeamId(team2.getId());

            //then
            assertThat(list).hasSize(2);
            assertThat(list2).isEmpty();
        }

        //팀 id, member id를 통해 조회
        @Test
        @DisplayName("팀 id, 멤버 id를 통해 조회")
        public void searchByTeamIdAndMemberId() {
            Member member1 = em.persist(createMember());
            Member member2 = em.persist(createMember());

            Team team = em.persist(createTeam("팀1"));

            Participant participant1 = em.persist(createParticipant(team, member1, TeamMemberRole.ORDERER));
            Participant participant2 = em.persist(createParticipant(team, member2, TeamMemberRole.ORDERER));

            //when
            Optional<Participant> result = participantRepository.findByTeamIdAndMemberId(1L, 1L);
            Optional<Participant> result2 = participantRepository.findByTeamIdAndMemberId(1L, 2L);
            Optional<Participant> result3 = participantRepository.findByTeamIdAndMemberId(1L, 3L);
            Optional<Participant> result4 = participantRepository.findByTeamIdAndMemberId(2L, 1L);

            //then
            assertThat(result).isEqualTo(Optional.of(participant1));
            assertThat(result2).isEqualTo(Optional.of(participant2));
            assertThat(result3).isNotPresent();
            assertThat(result4).isNotPresent();
        }

        // 수정 필요
        @Disabled
        @Test
        @DisplayName("해당 팀에 역할을 조회")
        public void searchByTeamIdAndRole() {
            //given
            Team team1 = em.persist(createTeam("팀1"));
            Team team2 = em.persist(createTeam("팀2"));
            Member member1 = em.persist(createMember());
            Member member2 = em.persist(createMember());

            em.persist(createParticipant(team1, member1, TeamMemberRole.ADMIN));
            em.persist(createParticipant(team1, member2, TeamMemberRole.ORDERER));
            em.persist(createParticipant(team2, member1, TeamMemberRole.ORDERER));
            em.persist(createParticipant(team2, member2, TeamMemberRole.ORDERER));
            //when
            //team에는 admin이 있고
            Optional<Participant> result1 = participantRepository.findByTeamIdAndTeamMemberRole(team1.getId(),
                    TeamMemberRole.ADMIN);
            Optional<Participant> result2 = participantRepository.findByTeamIdAndTeamMemberRole(team1.getId(),
                    TeamMemberRole.VIEWER);

            //team2에는 admin이 없다
            Optional<Participant> result3 = participantRepository.findByTeamIdAndTeamMemberRole(team2.getId(),
                    TeamMemberRole.ADMIN);
            Optional<Participant> result4 = participantRepository.findByTeamIdAndTeamMemberRole(team2.getId(),
                    TeamMemberRole.ORDERER);

            //then
            assertThat(result1).isPresent();
            assertThat(result2).isEmpty();
            assertThat(result3).isEmpty();
            assertThat(result4).isPresent();
        }
    }


    @Nested
    @DisplayName("getMemberJoinTeam 메서드는")
    class Describe_getMemberJoinTeam {
        @Nested
        @DisplayName("사용자가 어느 팀에도 소속되어 있지 않다면")
        class Context_with_no_participation {
            Long memberId;

            @BeforeEach
            void setUpContext() {
                memberId = em.persistAndGetId(createMember(), Long.class);
            }

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_empty_list() {
                var result = participantRepository.getMemberJoinTeam(memberId);
                Assertions.assertThat(result)
                        .isEmpty();
            }
        }

        @Nested
        @DisplayName("사용자가 팀에 소속되어 있다면")
        class Context_with_participation {
            Long memberId;

            @BeforeEach
            void setUpContext() {
                Member member = em.persist(createMember());
                memberId = member.getId();
                Team team1 = em.persist(createTeam("팀1"));
                Team team2 = em.persist(createTeam("팀2"));
                em.persist(createParticipant(team1, member, TeamMemberRole.ORDERER));
                em.persist(createParticipant(team2, member, TeamMemberRole.ORDERER));
            }

            @Test
            @DisplayName("반환하는 리스트의 크기는 팀 개수와 일치해야 한다.")
            void it_returns_with_same_size_as_team_count() {
                var result = participantRepository.getMemberJoinTeam(memberId);
                assertThat(result.size()).isEqualTo(2);
            }

            @Test
            @DisplayName("팀 아이디, 팀 이름, 팀에서 사용자 권한이 담긴 리스트를 반환해야 한다.")
            void it_returns_with() {
                var result = participantRepository.getMemberJoinTeam(memberId);
                result.forEach(info -> {
                    Assertions.assertThat(info.role())
                            .isNotNull();
                    Assertions.assertThat(info.teamId())
                            .isNotNull();
                    Assertions.assertThat(info.teamName())
                            .isNotNull();
                });
            }
        }
    }
}
