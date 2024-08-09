package com.yellobook.domain.team;

import com.yellobook.common.enums.MemberRole;
import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.config.TestConfig;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.domains.team.repository.ParticipantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@ContextConfiguration(classes = TestConfig.class)
@DisplayName("Participant Repository 테스트")
public class ParticipantRepositoryTest {
    @Autowired
    private ParticipantRepository participantRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        participantRepository.deleteAll();
        entityManager.createNativeQuery("ALTER TABLE participants ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    @Test
    @DisplayName("참가자 생성")
    public void addParticipant() {
        //given
        Team team = Team.builder()
                .name("team")
                .address("서울 강남구")
                .phoneNumber("010010101").build();
        entityManager.persist(team);

        Member member = new Member(null,"설","johndoe@gmail.com","", MemberRole.USER);
        member.updateAllowance();
        entityManager.persist(member);

        Participant participant = new Participant(team, member, MemberTeamRole.ADMIN);
        //when
        participantRepository.save(participant);

        //then
        Assertions.assertThat(participantRepository.findAll()).hasSize(1);
        Assertions.assertThat(participantRepository.findById(participant.getId())).isPresent();
    }

    @Test
    @DisplayName("참가자 삭제")
    public void deleteParticipant() {
        //given
        Team team = Team.builder()
                .name("team")
                .address("서울 강남구")
                .phoneNumber("010010101").build();
        entityManager.persist(team);

        Member member = new Member(null,"설","johndoe@gmail.com","", MemberRole.USER);
        member.updateAllowance();
        entityManager.persist(member);

        Participant participant = new Participant(team, member, MemberTeamRole.ADMIN);

        participantRepository.save(participant);

        //when
        participantRepository.deleteById(participant.getId());

        //then
        Assertions.assertThat(participantRepository.findAll()).isEmpty();
    }
}
