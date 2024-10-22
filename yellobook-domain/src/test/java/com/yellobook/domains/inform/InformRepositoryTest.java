package com.yellobook.domains.inform;

import static fixture.InformFixture.createInform;
import static fixture.MemberFixture.createMember;
import static fixture.TeamFixture.createTeam;
import static org.assertj.core.api.Assertions.assertThat;

import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.repository.InformRepository;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.support.annotation.RepositoryTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
public class InformRepositoryTest {

    @Autowired
    private InformRepository informRepository;

    @PersistenceContext
    private EntityManager em;

    @Nested
    @DisplayName("InformRepository의 save 메소드는")
    class Describe_Inform_Repository_Save {

        @Nested
        @DisplayName("유효한 inform을 저장하고자 하는 경우")
        class Context_valid_inform {

            Member member;
            Team team;
            Inform inform;
            Inform savedInform;

            @BeforeEach
            void setUp() {
                member = createMember();
                team = createTeam();
                inform = createInform(team, member);

                em.persist(member);
                em.persist(team);
                em.persist(inform);

                savedInform = informRepository.save(inform);
            }

            @Test
            @DisplayName("inform을 저장한다.")
            void it_returns_inform_saved() {
                assertThat(savedInform.getId()).isEqualTo(inform.getId());
                assertThat(savedInform.getTitle()).isEqualTo(inform.getTitle());
                assertThat(savedInform.getContent()).isEqualTo(inform.getContent());
            }
        }
    }

    @Nested
    @DisplayName("deleteById 메소드는")
    class Describe_DeleteById {

        @Nested
        @DisplayName("inform이 존재하는 경우")
        class Context_exist_inform {

            Member member;
            Team team;
            Inform inform;
            Long informId;

            @BeforeEach
            void setUp() {
                member = createMember();
                team = createTeam();
                inform = createInform(team, member);

                em.persist(member);
                em.persist(team);
                em.persist(inform);

                informId = inform.getId();
            }

            @Test
            @DisplayName("inform을 삭제한다.")
            void it_returns_inform_deleted() {
                informRepository.deleteById(informId);

                assertThat(informRepository.findById(informId)).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("InformRepository의 findById 메소드는")
    class Describe_FindById {

        @Nested
        @DisplayName("inform이 존재하면")
        class Context_exist_inform {

            Member member;
            Team team;
            Inform inform;
            Long informId;

            @BeforeEach
            void setUp() {
                member = createMember();
                team = createTeam();
                inform = createInform(team, member);

                em.persist(member);
                em.persist(team);
                em.persist(inform);

                informId = inform.getId();
            }

            @Test
            @DisplayName("inform을 반환한다.")
            void it_returns_inform() {
                Optional<Inform> foundInform = informRepository.findById(informId);

                assertThat(foundInform.isPresent()).isTrue();
                assertThat(foundInform).isEqualTo(Optional.of(inform));
            }
        }

        @Nested
        @DisplayName("inform이 존재하지 않으면")
        class Context_not_exist_inform {

            Long nonExistentInformId = 99L;

            @Test
            @DisplayName("Optional.empty()를 반환한다.")
            void it_returns_OptionalEmpty() {
                Optional<Inform> foundInform = informRepository.findById(nonExistentInformId);

                assertThat(foundInform).isEmpty();
            }
        }
    }
}
