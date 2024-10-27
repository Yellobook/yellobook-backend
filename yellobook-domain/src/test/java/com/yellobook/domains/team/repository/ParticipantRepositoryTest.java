package com.yellobook.domains.team.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.yellobook.common.enums.MemberRole;
import com.yellobook.common.enums.TeamMemberRole;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.dto.query.QueryTeamMember;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.support.RepositoryTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("ParticipantRepository Unit Test")
public class ParticipantRepositoryTest extends RepositoryTest {

    @Autowired
    private ParticipantRepository participantRepository;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    void setUp() {
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

        Participant participant = new Participant(team, member, TeamMemberRole.ADMIN);
        //when
        participantRepository.save(participant);

        //then
        assertThat(participantRepository.findAll()).hasSize(1);
        assertThat(participantRepository.findById(participant.getId())).isPresent();
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
        assertThat(participantRepository.findAll()).isEmpty();
    }

    private Participant createParticipant(String nickname, String email, MemberRole role, Team team,
                                          TeamMemberRole teamRole) {
        Member member = Member.builder()
                .nickname(nickname)
                .email(email)
                .role(role)
                .build();
        member.updateAllowance();
        em.persist(member);

        Participant participant = Participant.builder()
                .team(team)
                .member(member)
                .teamMemberRole(teamRole)
                .build();
        em.persist(participant);

        return participant;
    }

    //이름을 넣어서 리스트로 받는 조회
    @Nested
    @DisplayName("참가자 조회")
    public class searchParticipant {
        @Test
        @DisplayName("검색어를 통해 리스트로 받는 조회")
        public void searchParticipantByPrefix() {
            // Given
            String prefix = "홍";

            Team team = Team.builder()
                    .name("Development")
                    .phoneNumber("123-456-7890")
                    .address("1234 Test St")
                    .build();
            em.persist(team);

            Long teamId = 1L;//team.getId();

            Participant participant1 = createParticipant(
                    "홍길동",
                    "hong@example.com",
                    MemberRole.USER,
                    team,
                    TeamMemberRole.ADMIN
            );

            Participant participant2 = createParticipant(
                    "홍명보",
                    "bo@example.com",
                    MemberRole.USER,
                    team,
                    TeamMemberRole.ORDERER
            );

            Participant participant3 = createParticipant(
                    "김철수",
                    "kim@example.com",
                    MemberRole.USER,
                    team,
                    TeamMemberRole.ORDERER
            );

            Participant participant4 = createParticipant(
                    "김홍도",
                    "kim1@example.com",
                    MemberRole.USER,
                    team,
                    TeamMemberRole.ORDERER
            );

            // When
            List<QueryTeamMember> result = participantRepository.findMentionsByNamePrefix(prefix, teamId);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result.get(0)
                    .nickname()).isEqualTo("홍길동");
            assertThat(result.get(1)
                    .nickname()).isEqualTo("홍명보");
        }

        //팀 id를 통해 모든 참가자를 받는 조회
        @Test
        @DisplayName("팀 id를 통해 모든 참가자 조회")
        public void searchByTeamId() {
            //given
            Team team = Team.builder()
                    .name("Development")
                    .phoneNumber("123-456-7890")
                    .address("1234 Test St")
                    .build();
            em.persist(team);

            Long teamId = 1L;

            Participant participant1 = createParticipant(
                    "홍길동",
                    "hong@example.com",
                    MemberRole.USER,
                    team,
                    TeamMemberRole.ADMIN
            );

            Participant participant2 = createParticipant(
                    "김철수",
                    "kim@example.com",
                    MemberRole.USER,
                    team,
                    TeamMemberRole.ORDERER
            );
            em.flush();

            //when
            List<Participant> list = participantRepository.findAllByTeamId(teamId);
            List<Participant> list2 = participantRepository.findAllByTeamId(teamId + 1L);

            //then
            assertThat(list).hasSize(2);
            assertThat(list2).isEmpty();
        }

        //팀 id, member id를 통해 조회
        @Test
        @DisplayName("팀 id, 멤버 id를 통해 조회")
        public void searchByTeamIdAndMemberId() {
            //given
            Team team = Team.builder()
                    .name("Development")
                    .phoneNumber("123-456-7890")
                    .address("1234 Test St")
                    .build();
            em.persist(team);

            Long teamId = 1L;

            Participant participant1 = createParticipant(
                    "홍길동",
                    "hong@example.com",
                    MemberRole.USER,
                    team,
                    TeamMemberRole.ORDERER
            );

            Participant participant2 = createParticipant(
                    "김철수",
                    "kim@example.com",
                    MemberRole.USER,
                    team,
                    TeamMemberRole.ORDERER
            );
            em.flush();

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

        //팀 id, 역할로 조회
        @Test
        @DisplayName("해당 팀에 역할을 조회")
        public void searchByTeamIdAndRole() {
            //given
            Team team = Team.builder()
                    .name("Development")
                    .phoneNumber("123-456-7890")
                    .address("1234 Test St")
                    .build();
            em.persist(team);

            Team team2 = Team.builder()
                    .name("아디다스")
                    .phoneNumber("123-456-789")
                    .address("경기도")
                    .build();
            em.persist(team2);

            Participant participant1 = createParticipant(
                    "홍길동",
                    "hong@example.com",
                    MemberRole.USER,
                    team,
                    TeamMemberRole.ADMIN
            );

            Participant participant2 = createParticipant(
                    "홍명보",
                    "bo@example.com",
                    MemberRole.USER,
                    team,
                    TeamMemberRole.ORDERER
            );

            Participant participant3 = createParticipant(
                    "김철수",
                    "kim@example.com",
                    MemberRole.USER,
                    team2,
                    TeamMemberRole.VIEWER
            );

            Participant participant4 = createParticipant(
                    "김홍도",
                    "kim1@example.com",
                    MemberRole.USER,
                    team2,
                    TeamMemberRole.ORDERER
            );
            //when
            //team에는 admin이 있고
            Optional<Participant> result1 = participantRepository.findByTeamIdAndTeamMemberRole(team.getId(),
                    TeamMemberRole.ADMIN);
            Optional<Participant> result2 = participantRepository.findByTeamIdAndTeamMemberRole(team.getId(),
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
}
