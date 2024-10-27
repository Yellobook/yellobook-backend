package com.yellobook.domains.team.repository;

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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@DisplayName("TeamRepositoryTest Unit Test")
public class TeamRepositoryTest extends RepositoryTest {
    @Autowired
    private TeamRepository teamRepository;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    void setUp() {
        resetAutoIncrement();
    }

    @Test
    @DisplayName("팀 생성")
    public void teamRepository_SaveAll_ReturnSavedTeam() {

        //Arrange
        Team team = Team.builder()
                .name("team")
                .address("서울 강남구")
                .phoneNumber("010010101")
                .build();

        //Act
        Team savedTeam = teamRepository.save(team);

        //Assert
        Assertions.assertThat(savedTeam.getId())
                .isNotNull();
        Assertions.assertThat(savedTeam.getId())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("팀 조회")
    public void searchTeam() {
        //given
        Team team = Team.builder()
                .name("team")
                .address("서울 강남구")
                .phoneNumber("0101")
                .build();
        Team team2 = Team.builder()
                .name("team2")
                .address("서울")
                .phoneNumber("010")
                .build();

        //when
        Team t1 = teamRepository.save(team);
        Team t2 = teamRepository.save(team2);

        //then
        Assertions.assertThat(t1.getId())
                .isEqualTo(1);
        Assertions.assertThat(teamRepository.findAll()
                        .size())
                .isEqualTo(2);
    }

    @Test
    @DisplayName("중복된 팀 이름으로 저장할 때 예외 발생")
    public void findSameNameTeam() {
        //given
        Team team = Team.builder()
                .name("team")
                .address("서울 강남구")
                .phoneNumber("0101")
                .build();

        Team team2 = Team.builder()
                .name("team")
                .address("서울 강남구")
                .phoneNumber("0101")
                .build();
        //when
        teamRepository.save(team);

        //then
        Assertions.assertThatThrownBy(() -> teamRepository.save(team2))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("could not execute statement");
        em.clear();
        Assertions.assertThat(teamRepository.findAll())
                .hasSize(1);
    }

    @Test
    @DisplayName("모든 팀원 조회")
    public void findAllTeamMembers() {
        //given
        Long teamId = 1L;
        Team team = Team.builder()
                .name("Development")
                .phoneNumber("123-456-7890")
                .address("1234 Test St")
                .build();
        em.persist(team);

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

        //when
        List<QueryTeamMember> members = teamRepository.findTeamMembers(teamId);

        //then
        Assertions.assertThat(members.size())
                .isEqualTo(4);
    }

    private Participant createParticipant(String nickname, String email, MemberRole role, Team team,
                                          TeamMemberRole teamMemberRole) {
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
                .teamMemberRole(teamMemberRole)
                .build();
        em.persist(participant);

        return participant;
    }
}
