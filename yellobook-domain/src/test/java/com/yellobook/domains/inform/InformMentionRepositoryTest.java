package com.yellobook.domains.inform;

import static fixture.InformFixture.createInform;
import static fixture.MemberFixture.createMember;
import static fixture.TeamFixture.createTeam;
import static org.assertj.core.api.Assertions.assertThat;

import com.yellobook.common.enums.MemberRole;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformMention;
import com.yellobook.domains.inform.repository.InformMentionRepository;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.support.annotation.RepositoryTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
public class InformMentionRepositoryTest {

    @Autowired
    private InformMentionRepository informMentionRepository;

    @PersistenceContext
    private EntityManager em;

    @Nested
    @DisplayName("saveAll 메소드는")
    class Describe_SaveAll {

        @Nested
        @DisplayName("유효한 mention 리스트가 주어졌을 때")
        class Context_valid {

            Member member;
            Team team;
            Inform inform;
            List<InformMention> mentions;
            Member member2;

            @BeforeEach
            void setUp() {
                member = createMember();
                team = createTeam();

                em.persist(team);
                em.persist(member);

                inform = createInform(team, member);
                em.persist(inform);

                member2 = Member.builder()
                        .nickname("test2")
                        .allowance(true)
                        .email("tt@tt")
                        .role(MemberRole.USER)
                        .profileImage("ds.png")
                        .build();

                em.persist(member2);

                InformMention mention1 = InformMention.builder()
                        .inform(inform)
                        .member(member)
                        .build();
                InformMention mention2 = InformMention.builder()
                        .inform(inform)
                        .member(member2)
                        .build();

                mentions = List.of(mention1, mention2);
            }

            @Test
            @DisplayName("모든 InformMention을 저장한다.")
            void it_returns_save_all_mentions() {
                List<InformMention> savedMentions = informMentionRepository.saveAll(mentions);

                assertThat(savedMentions).isNotEmpty();
                assertThat(savedMentions.size()).isEqualTo(2);
            }
        }

        @Nested
        @DisplayName("빈 리스트가 주어졌을 때")
        class Context_empty {

            List<InformMention> mentions;

            @BeforeEach
            void setUp() {
                mentions = List.of();
            }

            @Test
            @DisplayName("아무 것도 저장하지 않는다.")
            void it_returns_empty_list() {
                List<InformMention> savedMentions = informMentionRepository.saveAll(mentions);

                assertThat(savedMentions).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("findByInformId 메소드는")
    class Describe_FindAllByInformId {

        @Nested
        @DisplayName("해당하는 inform 안에 mention 이 존재하는 경우")
        class Context_exist_informmention {

            Member member;
            Team team;
            Inform inform;
            Long informId;
            List<InformMention> mentions;

            @BeforeEach
            void setUp() {
                member = createMember();
                team = createTeam();

                em.persist(team);
                em.persist(member);

                inform = createInform(team, member);
                em.persist(inform);
                informId = inform.getId();

                InformMention informMention = InformMention.builder()
                        .inform(inform)
                        .member(member)
                        .build();

                em.persist(informMention);
            }

            @Test
            @DisplayName("InformMention list를 반환한다.")
            void it_returns_inform_mention_list() {
                mentions = informMentionRepository.findAllByInformId(informId);

                assertThat(mentions).isNotEmpty();
            }
        }

        @Nested
        @DisplayName("해당하는 inform 안에 mention이 존재하지 않는 경우")
        class Context_not_exist_informmention {

            Member member;
            Team team;
            Inform inform;
            Long informId;
            List<InformMention> mentions;

            @BeforeEach
            void setUp() {
                member = createMember();
                team = createTeam();

                em.persist(team);
                em.persist(member);

                inform = createInform(team, member);
                em.persist(inform);
                informId = inform.getId();
                mentions = informMentionRepository.findAllByInformId(informId);
            }

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_empty_mention_list() {
                assertThat(mentions).isEmpty();
            }
        }

        @Nested
        @DisplayName("Inform이 존재하지 않는 경우")
        class Context_not_exist_inform {

            Long nonExistentInformId = 99L;
            List<InformMention> mentions;

            @BeforeEach
            void setUp() {
                mentions = informMentionRepository.findAllByInformId(nonExistentInformId);
            }

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_empty_mention_list() {
                assertThat(mentions).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("deleteByInformId 메소드는")
    class Describe_DeleteByInformId {

        @Nested
        @DisplayName("informId에 해당하는 mention이 존재하는 경우")
        class Context_exist_informmention {

            Member member;
            Team team;
            Inform inform;
            Long informId;

            @BeforeEach
            void setUp() {
                member = createMember();
                team = createTeam();

                em.persist(team);
                em.persist(member);

                inform = createInform(team, member);
                em.persist(inform);
                informId = inform.getId();

                InformMention informMention = InformMention.builder()
                        .inform(inform)
                        .member(member)
                        .build();

                em.persist(informMention);
            }

            @Test
            @DisplayName("해당 inform에 있는 모든 mention을 지운다.")
            void it_returns_delete_inform_mention() {
                informMentionRepository.deleteByInformId(informId);

                List<InformMention> mentions = informMentionRepository.findAllByInformId(informId);
                assertThat(mentions).isEmpty();
            }
        }

        @Nested
        @DisplayName("informId에 해당하는 mention이 존재하지 않은 경우")
        class Context_not_exist_informmention {

            Long nonExistentInformId;

            @BeforeEach
            void setUp() {
                nonExistentInformId = 999L;
            }

            @Test
            @DisplayName("아무 것도 삭제하지 않는다.")
            void it_does_nothing() {

                //deleteByInformId 호출
                informMentionRepository.deleteByInformId(nonExistentInformId);
                em.flush();
                em.clear();

                //에러 없이 정상적으로 메서드가 수행되어야 함
                List<InformMention> mentions = informMentionRepository.findAllByInformId(nonExistentInformId);
                assertThat(mentions).isEmpty();  // 빈 리스트가 반환되어야 함
            }
        }
    }
}
