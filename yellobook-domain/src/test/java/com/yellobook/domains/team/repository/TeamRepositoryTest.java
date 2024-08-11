package com.yellobook.domains.team.repository;

import com.yellobook.config.TeamRepositoryTestConfig;
import com.yellobook.domains.team.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TeamRepositoryTestConfig.class)
@DisplayName("teamRepository 테스트")
public class TeamRepositoryTest {
    @Autowired
    private TeamRepository teamRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void setUp(){
        teamRepository.deleteAll();
        entityManager.createNativeQuery("ALTER TABLE teams ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    @Test
    @DisplayName("팀 생성")
    public void teamRepository_SaveAll_ReturnSavedTeam() {

        //Arrange
        Team team = Team.builder()
                .name("team")
                .address("서울 강남구")
                .phoneNumber("010010101").build();

        //Act
        Team savedTeam = teamRepository.save(team);

        //Assert
        Assertions.assertThat(savedTeam.getId()).isNotNull();
        Assertions.assertThat(savedTeam.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("팀 조회")
    public void searchTeam(){
        //given
        Team team = Team.builder()
                .name("team")
                .address("서울 강남구")
                .phoneNumber("0101").build();
        Team team2 = Team.builder()
                .name("team2")
                .address("서울")
                .phoneNumber("010").build();

        //when
        Team t1 = teamRepository.save(team);
        Team t2 = teamRepository.save(team2);

        //then
        Assertions.assertThat(t1.getId()).isEqualTo(1);
        Assertions.assertThat(teamRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("중복된 팀 이름으로 저장할 때 예외 발생")
    public void findSameNameTeam(){
        //given
        Team team = Team.builder()
                .name("team")
                .address("서울 강남구")
                .phoneNumber("0101").build();

        Team team2 = Team.builder()
                .name("team")
                .address("서울 강남구")
                .phoneNumber("0101").build();
        //when
        teamRepository.save(team);

        //then
        Assertions.assertThatThrownBy(() -> teamRepository.save(team2))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("could not execute statement");
        entityManager.clear();
        Assertions.assertThat(teamRepository.findAll()).hasSize(1);
    }
}
