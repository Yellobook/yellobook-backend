package com.yellobook.domains.inform;

import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformComment;
import com.yellobook.domains.inform.repository.InformCommentRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
public class InformCommentRepositoryTest {

    @Autowired
    private InformCommentRepository informCommentRepository;

    @PersistenceContext
    private EntityManager em;

    private Member member;
    private Team team;
    private Inform inform;
    private Long informId;

    @BeforeEach
    void setUp() {
        member = MemberFixture.createMember();
        team = TeamFixture.createTeam();

        em.persist(team);
        em.persist(member);

        inform = InformFixture.createInform(team, member);
        em.persist(inform);

        informId = inform.getId();
    }

    @Nested
    @DisplayName("save 메소드는")
    class Describe_Save{

        @Nested
        @DisplayName("유효한 요청인 경우")
        class Context_Valid{

            InformComment comment;
            InformComment result;

            @BeforeEach
            void setUp() {
                comment = InformComment.builder().inform(inform).member(member).content("test").build();

                result = informCommentRepository.save(comment);
            }

            @Test
            @DisplayName("comment를 저장한다.")
            void it_returns_save_inform_comment(){
                assertThat(result).isNotNull();
                assertThat(result).isEqualTo(comment);
            }
        }
    }

    @Nested
    @DisplayName("FindByInformId 메소드는")
    class Describe_FindByInformId{

        @Nested
        @DisplayName("해당하는 inform 안에 comment 가 존재하는 경우")
        class Context_Exist_InformComment{

            List<InformComment> list;

            @BeforeEach
            void setUp() {
                InformComment comment = InformComment.builder().inform(inform).member(member).content("test").build();
                em.persist(comment);
            }

            @Test
            @DisplayName("InformComment list를 반환한다.")
            void it_returns_inform_comment_list(){
                list = informCommentRepository.findByInformId(informId);

                assertThat(list).isNotEmpty();
            }
        }

        @Nested
        @DisplayName("해당하는 inform 안에 comment 가 존재하지 않는 경우")
        class Context_Not_Exist_InformComment{

            List<InformComment> list;

            @BeforeEach
            void setUp(){
                list = informCommentRepository.findByInformId(informId);
            }

            @Test
            @DisplayName("empty list를 반환한다.")
            void it_returns_empty_comment_list(){
                assertThat(list).isEmpty();
            }
        }

        @Nested
        @DisplayName("Inform이 존재하지 않는 경우")
        class Context_Not_Exist_Inform{

            Long nonExistInformId;
            List<InformComment> list;

            @BeforeEach
            void setUp(){
                nonExistInformId = 99L;
                list = informCommentRepository.findByInformId(nonExistInformId);
            }

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_empty_comment_list(){
                assertThat(list).isEmpty();
            }
        }
    }
}
