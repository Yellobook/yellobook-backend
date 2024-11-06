package com.yellobook.domains.inform;

import static fixture.InformFixture.createInform;
import static fixture.MemberFixture.createMember;
import static fixture.TeamFixture.createTeam;
import static org.assertj.core.api.Assertions.assertThat;

import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformComment;
import com.yellobook.domains.inform.repository.InformCommentRepository;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.support.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("InformCommentRepository Unit Test")
public class InformCommentRepositoryTest extends RepositoryTest {

    @Autowired
    private InformCommentRepository informCommentRepository;

    @BeforeEach
    public void setUp() {
        resetAutoIncrement();
    }

    @Nested
    @DisplayName("save 메소드는")
    class Describe_Save {

        @Nested
        @DisplayName("유효한 요청인 경우")
        class Context_valid {

            Member member;
            Team team;
            Inform inform;
            InformComment comment;
            InformComment result;

            @BeforeEach
            void setUp() {
                member = em.persist(createMember());
                team = em.persist(createTeam("팀1"));
                inform = em.persist(createInform(team, member));

                comment = InformComment.builder()
                        .inform(inform)
                        .member(member)
                        .content("test")
                        .build();

                result = informCommentRepository.save(comment);
            }

            @Test
            @DisplayName("comment를 저장한다.")
            void it_returns_save_inform_comment() {
                assertThat(result).isNotNull();
                assertThat(result).isEqualTo(comment);
            }
        }
    }

    @Nested
    @DisplayName("FindByInformId 메소드는")
    class Describe_FindByInformId {

        @Nested
        @DisplayName("해당하는 inform 안에 comment 가 존재하는 경우")
        class Context_exist_informcomment {
            Long informId;
            List<InformComment> list;

            @BeforeEach
            void setUp() {
                Member member = em.persist(createMember());
                Team team = em.persist(createTeam("팀1"));
                Inform inform = em.persist(createInform(team, member));

                informId = inform.getId();
                InformComment comment = InformComment.builder()
                        .inform(inform)
                        .member(member)
                        .content("test")
                        .build();
                em.persist(comment);
            }

            @Test
            @DisplayName("InformComment list를 반환한다.")
            void it_returns_inform_comment_list() {
                list = informCommentRepository.findByInformId(informId);

                assertThat(list).isNotEmpty();
            }
        }

        @Nested
        @DisplayName("해당하는 inform 안에 comment 가 존재하지 않는 경우")
        class Context_not_exist_informcomment {

            Long informId;

            List<InformComment> list;

            @BeforeEach
            void setUp() {
                Member member = em.persist(createMember());
                Team team = em.persist(createTeam("팀1"));
                Inform inform = em.persist(createInform(team, member));

                informId = inform.getId();
                list = informCommentRepository.findByInformId(informId);
            }

            @Test
            @DisplayName("empty list를 반환한다.")
            void it_returns_empty_comment_list() {
                assertThat(list).isEmpty();
            }
        }

        @Nested
        @DisplayName("Inform이 존재하지 않는 경우")
        class Context_not_exist_inform {

            Long nonExistInformId;
            List<InformComment> list;

            @BeforeEach
            void setUp() {
                nonExistInformId = 99L;
                list = informCommentRepository.findByInformId(nonExistInformId);
            }

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_empty_comment_list() {
                assertThat(list).isEmpty();
            }
        }
    }
}
