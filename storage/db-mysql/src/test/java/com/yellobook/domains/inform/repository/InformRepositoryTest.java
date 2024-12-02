package com.yellobook.domains.inform.repository;

import static fixture.InformFixture.createInform;
import static fixture.MemberFixture.createMember;
import static fixture.TeamFixture.createTeam;
import static org.assertj.core.api.Assertions.assertThat;

import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.support.RepositoryTest;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("InformRepository Unit Test")
public class InformRepositoryTest extends RepositoryTest {

    @Autowired
    private InformRepository informRepository;

    @BeforeEach
    public void setUp() {
        resetAutoIncrement();
    }

    @Nested
    @DisplayName("save 메소드는")
    class Describe_Inform_Repository_Save {

        @Nested
        @DisplayName("유효한 공지를 저장하고자 하는 경우")
        class Context_valid_inform {
            Inform inform;
            Inform savedInform;

            @BeforeEach
            void setUp() {
                Member member = em.persist(createMember());
                Team team = em.persist(createTeam("팀1"));
                inform = createInform(team, member);
                savedInform = informRepository.save(createInform(team, member));
            }

            @Test
            @DisplayName("inform을 저장한다.")
            void it_returns_inform_saved() {
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
            Long informId;

            @BeforeEach
            void setUp() {
                Member member = em.persist(createMember());
                Team team = em.persist(createTeam("팀1"));
                informId = em.persistAndGetId(createInform(team, member), Long.class);
            }

            @Test
            @DisplayName("inform을 삭제한다.")
            void it_returns_inform_deleted() {
                informRepository.deleteById(informId);

                Assertions.assertThat(informRepository.findById(informId))
                        .isEmpty();
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
                member = em.persist(createMember());
                team = em.persist(createTeam("팀1"));
                inform = em.persist(createInform(team, member));

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
