package com.yellobook.domains.inform;

import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.repository.InformRepository;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.support.annotation.RepositoryTest;
import fixture.InformFixture;
import fixture.MemberFixture;
import fixture.TeamFixture;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RepositoryTest
public class InformRepositoryTest {

    @Autowired
    private InformRepository informRepository;

    Member member;
    Team team;
    Inform inform;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public InformRepositoryTest(InformRepository informRepository,
                                EntityManager em) {
        this.informRepository = informRepository;
        this.em = em;
    }

    @BeforeEach
    void setUp() {
        member = MemberFixture.createMember();
        team = TeamFixture.createTeam();
        inform = InformFixture.createInform(team, member);

        em.persist(member);
        em.persist(team);
        em.persist(inform);
    }


    @Nested
    @DisplayName("InformRepository의 save 메소드는")
    class Describe_Inform_Repository_Save{

        @Nested
        @DisplayName("유효한 inform을 저장하고자 하는 경우")
        class Context_Valid_Inform{

            Inform savedInform;

            @BeforeEach
            void setUp() {
                savedInform = informRepository.save(inform);
            }

            @Test
            @DisplayName("inform을 저장한다.")
            void it_returns_inform_saved(){
                assertThat(savedInform.getId()).isEqualTo(inform.getId());
                assertThat(savedInform.getTitle()).isEqualTo(inform.getTitle());
                assertThat(savedInform.getContent()).isEqualTo(inform.getContent());
            }
        }
    }

    @Nested
    @DisplayName("deleteById 메소드는")
    class Describe_DeleteById{

        @Nested
        @DisplayName("inform이 존재하는 경우")
        class Context_Exist_Inform{

            Long informId;

            @BeforeEach
            void setUp() {
                informId = inform.getId();
            }

            @Test
            @DisplayName("inform을 삭제한다.")
            void it_returns_inform_deleted(){
                informRepository.deleteById(informId);

                assertThat(informRepository.findById(informId)).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("InformRepository의 findById 메소드는")
    class Describe_FindById{

        @Nested
        @DisplayName("inform이 존재하면")
        class Context_Exist_Inform{

            Long informId;

            @BeforeEach
            void setUp() {
                informId = inform.getId();
            }

            @Test
            @DisplayName("inform을 반환한다.")
            void it_returns_inform(){
                Optional<Inform> foundInform = informRepository.findById(informId);

                assertThat(foundInform.isPresent()).isTrue();
                assertThat(foundInform).isEqualTo(Optional.of(inform));
            }
        }

        @Nested
        @DisplayName("inform이 존재하지 않으면")
        class Context_Not_Exist_Inform{

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
