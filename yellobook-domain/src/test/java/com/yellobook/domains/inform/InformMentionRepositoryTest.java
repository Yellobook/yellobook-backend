package com.yellobook.domains.inform;

import com.yellobook.common.enums.MemberRole;
import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformMention;
import com.yellobook.domains.inform.repository.InformMentionRepository;
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
public class InformMentionRepositoryTest {

    @Autowired
    private InformMentionRepository informMentionRepository;

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
    @DisplayName("saveAll 메소드는")
    class Describe_SaveAll{

        @Nested
        @DisplayName("유효한 mention 리스트가 주어졌을 때")
        class Context_Valid {

            List<InformMention> mentions;
            Member member2;

            @BeforeEach
            void setUp() {
                member2 = Member.builder()
                        .nickname("test2").allowance(true)
                        .email("tt@tt").role(MemberRole.USER)
                        .profileImage("ds.png").memberId(2L).build();

                em.merge(member2);

                InformMention mention1 = InformMention.builder().inform(inform).member(member).build();
                InformMention mention2 = InformMention.builder().inform(inform).member(member2).build();

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
        class Context_Empty {

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
    class Describe_FindByInformId{

        @Nested
        @DisplayName("해당하는 inform 안에 mention 이 존재하는 경우")
        class Context_Exist_InformMention{

            List<InformMention> mentions;

            @BeforeEach
            void setUp() {
                InformMention informMention = InformMention.builder().inform(inform).member(member).build();

                em.persist(informMention);
            }

            @Test
            @DisplayName("InformMention list를 반환한다.")
            void it_returns_inform_mention_list(){
                mentions = informMentionRepository.findByInformId(informId);

                assertThat(mentions).isNotEmpty();
            }
        }

        @Nested
        @DisplayName("해당하는 inform 안에 mention이 존재하지 않는 경우")
        class Context_Not_Exist_InformMention{

            List<InformMention> mentions;

            @BeforeEach
            void setUp() {
                mentions = informMentionRepository.findByInformId(informId);
            }

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_empty_mention_list(){
                assertThat(mentions).isEmpty();
            }
        }

        @Nested
        @DisplayName("Inform이 존재하지 않는 경우")
        class Context_Not_Exist_Inform{

            Long nonExistentInformId = 99L;
            List<InformMention> mentions;

            @BeforeEach
            void setUp() {
                mentions = informMentionRepository.findByInformId(nonExistentInformId);
            }

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_empty_mention_list(){
                assertThat(mentions).isEmpty();
            }
        }
    }
}
