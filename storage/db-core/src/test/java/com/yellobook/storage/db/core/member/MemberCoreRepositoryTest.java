package com.yellobook.storage.db.core.member;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.yellobook.core.domain.member.JoinedTeamResult;
import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.member.NewMember;
import com.yellobook.core.domain.member.ProfileInfo;
import com.yellobook.core.domain.member.SocialInfo;
import com.yellobook.core.enums.TeamMemberRole;
import com.yellobook.storage.db.core.team.ParticipantEntity;
import com.yellobook.storage.db.core.team.ParticipantJpaRepository;
import com.yellobook.storage.db.core.team.TeamEntity;
import com.yellobook.storage.db.core.team.TeamJpaRepository;
import com.yellobook.storage.db.support.RepositoryTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberCoreRepositoryTest extends RepositoryTest {
    @Autowired
    MemberCoreRepository memberRepository;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    TeamJpaRepository teamJpaRepository;

    @Autowired
    ParticipantJpaRepository participantJpaRepository;

    @Nested
    class save_메서드는 {

        @Nested
        class 새로운_회원_정보가_주어진_경우 {

            NewMember givenMember;

            @BeforeEach
            void setUpContext() {
                givenMember = new NewMember(
                        new ProfileInfo("김철수", "슈퍼 운영", "profile1.jpg", LocalDateTime.now()),
                        new SocialInfo("oauth_chulsoo", "naver", "chulsoo@naver.com")
                );
            }

            @Test
            void 회원을_저장하고_회원_ID를_반환한다() {
                Long result = memberRepository.save(givenMember);
                assertThat(result).isNotNull();
            }
        }
    }

    @Nested
    class findById_메서드는 {

        @Nested
        class 회원이_존재하는_경우 {

            long givenMemberId;

            @BeforeEach
            void setUpContext() {
                MemberEntity member = memberJpaRepository.save(
                        new MemberEntity("김철수", "슈퍼 운영", "chulsoo@naver.com",
                                "profile1.jpg", "oauth_chulsoo", "naver")
                );
                givenMemberId = member.getId();
            }

            @Test
            void 회원을_반환한다() {
                Optional<Member> result = memberRepository.findById(givenMemberId);
                assertThat(result).isPresent();
            }
        }

        @Nested
        class 회원이_존재하지_않는_경우 {
            final long givenMemberId = 999L;

            @Test
            void 빈_Optional을_반환한다() {
                assertThat(memberRepository.findById(givenMemberId)).isEmpty();
            }
        }
    }

    @Nested
    class findBySocialInfo_메서드는 {

        @Nested
        class 소셜_정보가_일치하는_경우 {

            SocialInfo givenSocialInfo;

            @BeforeEach
            void setUpContext() {
                MemberEntity member = memberJpaRepository.save(
                        new MemberEntity("김철수", "슈퍼 운영", "chulsoo@naver.com",
                                "profile1.jpg", "oauth_chulsoo", "naver")
                );
                givenSocialInfo = new SocialInfo(member.getOauthId(), member.getOauthProvider(), member.getEmail());
            }

            @Test
            void 회원을_반환한다() {
                assertThat(memberRepository.findBySocialInfo(givenSocialInfo)).isPresent();
            }
        }

        @Nested
        class 소셜_정보가_일치하지_않는_경우 {
            SocialInfo givenSocialInfo = new SocialInfo("unknown_oauth", "kakao", "unknown@kakao.com");

            @Test
            void 빈_Optional을_반환한다() {
                Optional<Member> result = memberRepository.findBySocialInfo(givenSocialInfo);
                assertThat(result).isEmpty();
            }
        }
    }

    @Nested
    class delete_메서드는 {

        @Nested
        class 회원이_존재하는_경우 {

            long givenMemberId;

            @BeforeEach
            void setUpContext() {
                MemberEntity member = memberJpaRepository.save(
                        new MemberEntity("김철수", "슈퍼 운영", "chulsoo@naver.com",
                                "profile1.jpg", "oauth_chulsoo", "naver")
                );
                givenMemberId = member.getId();
            }

            @Test
            void 회원을_삭제한다() {
                memberRepository.delete(new Member(givenMemberId, null, null));
                assertThat(memberJpaRepository.findById(givenMemberId)).isEmpty();
            }
        }
    }

    @Nested
    class updateNickname_메서드는 {

        @Nested
        class 회원이_존재하는_경우 {

            long givenMemberId;

            @BeforeEach
            void setUpContext() {
                MemberEntity member = memberJpaRepository.save(
                        new MemberEntity("김철수", "슈퍼 운영", "chulsoo@naver.com",
                                "profile1.jpg", "oauth_chulsoo", "naver")
                );
                givenMemberId = member.getId();
            }

            @Test
            void 닉네임을_변경한다() {
                memberRepository.updateNickname(new Member(givenMemberId, null, null), "철수네 마트");
                MemberEntity updatedMember = memberJpaRepository.findById(givenMemberId)
                        .get();
                assertThat(updatedMember.getNickname()).isEqualTo("철수네 마트");
            }
        }
    }

    @Nested
    class updateBio_메서드는 {

        @Nested
        class 회원이_존재하는_경우 {

            long givenMemberId;

            @BeforeEach
            void setUpContext() {
                MemberEntity member = memberJpaRepository.save(
                        new MemberEntity("김철수", "슈퍼 운영", "chulsoo@naver.com",
                                "profile1.jpg", "oauth_chulsoo", "naver")
                );
                givenMemberId = member.getId();
            }

            @Test
            void 자기소개를_변경한다() {
                memberRepository.updateBio(new Member(givenMemberId, null, null), "우리 동네 24시간 슈퍼");
                MemberEntity updatedMember = memberJpaRepository.findById(givenMemberId)
                        .get();
                assertThat(updatedMember.getBio()).isEqualTo("우리 동네 24시간 슈퍼");
            }
        }
    }


    @Nested
    class findJoinedTeamsByMemberId_메서드는 {

        @Nested
        class 사용자가_소속된_팀이_존재하는_경우 {

            final long givenMemberId = 1L;

            @BeforeEach
            void setUpContext() {
                MemberEntity member1 = new MemberEntity(
                        "김철수", "동네 슈퍼 운영", "chulsoo@naver.com",
                        "profile1.jpg", "oauth_chulsoo", "naver"
                );
                MemberEntity member2 = new MemberEntity(
                        "이영희", "핸드메이드 소품 판매", "younghee@kakao.com",
                        "profile2.jpg", "oauth_younghee", "kakao"
                );
                MemberEntity member3 = new MemberEntity(
                        "박민수", "동네 빵집 사장", "minsoo@naver.com",
                        "profile3.jpg", "oauth_minsoo", "naver"
                );
                MemberEntity member4 = new MemberEntity(
                        "정수진", "동네 카페 운영", "sujin@kakao.com",
                        "profile4.jpg", "oauth_sujin", "kakao"
                );
                memberJpaRepository.saveAll(List.of(member1, member2, member3, member4));

                TeamEntity team1 = new TeamEntity("철수네 슈퍼", "동네에서 운영하는 작은 슈퍼마켓", "010-1111-1111", "서울 강남구");
                TeamEntity team2 = new TeamEntity("영희네 소품샵", "핸드메이드 소품 전문점", "010-2222-2222", "서울 서초구");
                TeamEntity team3 = new TeamEntity("민수네 빵집", "동네에서 유명한 빵집", "010-3333-3333", "서울 마포구");
                teamJpaRepository.saveAll(List.of(team1, team2, team3));

                participantJpaRepository.saveAll(List.of(
                        new ParticipantEntity(team1, member1, TeamMemberRole.ORDERER),
                        new ParticipantEntity(team2, member1, TeamMemberRole.SELLER),
                        new ParticipantEntity(team3, member1, TeamMemberRole.VIEWER),
                        new ParticipantEntity(team1, member2, TeamMemberRole.SELLER),
                        new ParticipantEntity(team2, member2, TeamMemberRole.ORDERER),
                        new ParticipantEntity(team3, member2, TeamMemberRole.VIEWER),
                        new ParticipantEntity(team1, member3, TeamMemberRole.ORDERER),
                        new ParticipantEntity(team3, member3, TeamMemberRole.SELLER),
                        new ParticipantEntity(team3, member4, TeamMemberRole.ORDERER)
                ));
            }

            @Test
            void JoinedTeamResult_리스트를_반환한다() {
                List<JoinedTeamResult> result = memberRepository.findJoinedTeamsByMemberId(givenMemberId);

                assertThat(result)
                        .isNotEmpty()
                        .hasSize(3)
                        .extracting("teamId", "teamName", "myRole", "sellerName", "memberCount")
                        .containsExactlyInAnyOrder(
                                tuple(1L, "철수네 슈퍼", TeamMemberRole.ORDERER.name(), "이영희", 3),
                                tuple(2L, "영희네 소품샵", TeamMemberRole.SELLER.name(), "김철수", 2),
                                tuple(3L, "민수네 빵집", TeamMemberRole.VIEWER.name(), "박민수", 4)
                        );
            }
        }

        @Nested
        class 사용자가_아직_팀에_속하지_않았을_경우 {

            final long givenMemberId = 1L;

            @BeforeEach
            void setUpContext() {
                memberJpaRepository.save(new MemberEntity(
                        "김철수", "동네 슈퍼 운영", "chulsoo@naver.com",
                        "profile1.jpg", "oauth_chulsoo", "naver"
                ));
            }

            @Test
            void 빈_리스트를_반환한다() {
                List<JoinedTeamResult> result = memberRepository.findJoinedTeamsByMemberId(givenMemberId);
                assertThat(result.size()).isEqualTo(0);
            }
        }

    }
}