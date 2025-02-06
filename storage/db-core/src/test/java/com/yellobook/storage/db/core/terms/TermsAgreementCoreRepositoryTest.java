package com.yellobook.storage.db.core.terms;

import static org.assertj.core.api.Assertions.assertThat;

import com.yellobook.core.domain.terms.TermsAgreement;
import com.yellobook.core.domain.terms.TermsAgreementCheckResult;
import com.yellobook.storage.db.core.member.MemberEntity;
import com.yellobook.storage.db.core.member.MemberJpaRepository;
import com.yellobook.storage.db.support.RepositoryTest;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TermsAgreementCoreRepositoryTest extends RepositoryTest {

    @Autowired
    TermsAgreementCoreRepository termsAgreementRepository;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    TermsJpaRepository termsJpaRepository;

    @Autowired
    TermsItemJpaRepository termsItemJpaRepository;

    @Autowired
    TermsAgreementJpaRepository termsAgreementJpaRepository;

    @Autowired
    TermsItemAgreementJpaRepository termsItemAgreementJpaRepository;

    @Nested
    class hasMemberAgreedToActiveTerms_메서드는 {

        @Nested
        class 사용자가_활성화된_약관에_동의한_경우 {

            long memberId;

            @BeforeEach
            void setUpContext() {
                MemberEntity member = memberJpaRepository.save(
                        new MemberEntity("김철수", "슈퍼 운영", "chulsoo@naver.com",
                                "profile1.jpg", "oauth_chulsoo", "naver")
                );
                memberId = member.getId();

                TermsEntity terms = termsJpaRepository.save(
                        new TermsEntity("서비스 이용 약관", 1, true)
                );
                termsAgreementJpaRepository.save(
                        new TermsAgreementEntity(member, terms, LocalDateTime.now())
                );
            }

            @Test
            void true를_반환한다() {
                boolean result = termsAgreementRepository.hasMemberAgreedToActiveTerms(memberId);
                assertThat(result).isTrue();
            }
        }


        @Nested
        class 활성화된_약관이_없는_경우 {

            Long memberId;

            @BeforeEach
            void setUpContext() {
                MemberEntity member = memberJpaRepository.save(
                        new MemberEntity("김철수", "슈퍼 운영", "chulsoo@naver.com",
                                "profile1.jpg", "oauth_chulsoo", "naver")
                );
                memberId = member.getId();

                TermsEntity terms = termsJpaRepository.save(
                        new TermsEntity("서비스 이용 약관", 1, false)
                );

                termsAgreementJpaRepository.save(
                        new TermsAgreementEntity(member, terms, LocalDateTime.now())
                );
            }

            @Test
            void false를_반환한다() {
                boolean result = termsAgreementRepository.hasMemberAgreedToActiveTerms(memberId);
                assertThat(result).isFalse();
            }
        }


        @Nested
        class 사용자가_활성화된_약관에_동의하지_않은_경우 {

            long memberId;

            @BeforeEach
            void setUpContext() {
                MemberEntity member = memberJpaRepository.save(
                        new MemberEntity("김철수", "슈퍼 운영", "chulsoo@naver.com",
                                "profile1.jpg", "oauth_chulsoo", "naver")
                );
                termsJpaRepository.save(
                        new TermsEntity("서비스 이용 약관", 1, true)
                );

                memberId = member.getId();
            }

            @Test
            void false를_반환한다() {
                boolean result = termsAgreementRepository.hasMemberAgreedToActiveTerms(memberId);
                assertThat(result).isFalse();
            }
        }
    }

    @Nested
    class agreeToActiveTerms_메서드는 {

        @Nested
        class 사용자가_약관에_동의하는_경우 {

            long memberId;
            long termsId;
            List<Long> agreedItemIds;

            @BeforeEach
            void setUpContext() {
                MemberEntity member = memberJpaRepository.save(
                        new MemberEntity("김철수", "슈퍼 운영", "chulsoo@naver.com",
                                "profile1.jpg", "oauth_chulsoo", "naver")
                );
                memberId = member.getId();

                TermsEntity terms = termsJpaRepository.save(
                        new TermsEntity("서비스 이용 약관", 1, true)
                );
                termsId = terms.getId();

                TermsItemEntity termsItem1 = termsItemJpaRepository.save(
                        new TermsItemEntity(terms, "개인정보 수집",
                                "서비스 제공 및 운영을 위해 사용자의 개인정보를 수집 및 이용합니다.", TermsType.REQUIRED)
                );

                TermsItemEntity termsItem2 = termsItemJpaRepository.save(
                        new TermsItemEntity(terms, "마케팅 활용 동의",
                                "맞춤형 광고 및 프로모션 정보를 제공하기 위해 사용자의 정보를 활용합니다.", TermsType.OPTIONAL)
                );

                agreedItemIds = List.of(termsItem1.getId(), termsItem2.getId());
            }

            @Test
            @Transactional
            void 약관_동의_정보를_저장하고_객체를_반환한다() {
                TermsAgreement result = termsAgreementRepository.agreeToActiveTerms(memberId, termsId, agreedItemIds);

                assertThat(result).isNotNull();
                assertThat(result.memberId()).isEqualTo(memberId);
                assertThat(result.termsId()).isEqualTo(termsId);
                assertThat(result.agreeAt()).isNotNull();
            }
        }
    }

    @Nested
    class findAgreementCheckInfo_메서드는 {

        @Nested
        class 주어진_약관이_활성화된_약관이며_사용자가_처음_필수항목에_동의한_경우 {

            long memberId;
            long termsId;
            List<Long> agreedItemIds;

            @BeforeEach
            void setUpContext() {
                MemberEntity member = memberJpaRepository.save(
                        new MemberEntity("김철수", "슈퍼 운영", "chulsoo@naver.com",
                                "profile1.jpg", "oauth_chulsoo", "naver")
                );
                memberId = member.getId();

                TermsEntity terms = termsJpaRepository.save(
                        new TermsEntity("서비스 이용 약관", 1, true)
                );
                termsId = terms.getId();

                TermsItemEntity termsItem1 = termsItemJpaRepository.save(
                        new TermsItemEntity(terms, "개인정보 수집",
                                "서비스 제공 및 운영을 위해 사용자의 개인정보를 수집 및 이용합니다.", TermsType.REQUIRED)
                );

                TermsItemEntity termsItem2 = termsItemJpaRepository.save(
                        new TermsItemEntity(terms, "서비스 이용 약관 동의",
                                "서비스 이용과 관련된 기본적인 권리 및 의무 사항을 규정합니다.", TermsType.REQUIRED)
                );

                TermsItemEntity termsItem3 = termsItemJpaRepository.save(
                        new TermsItemEntity(terms, "마케팅 활용 동의",
                                "맞춤형 광고 및 프로모션 정보를 제공하기 위해 사용자의 정보를 활용합니다.", TermsType.OPTIONAL)
                );
                agreedItemIds = List.of(termsItem1.getId(), termsItem2.getId());
            }

            @Test
            void 올바른_약관_동의여부에_대한_결과를_반환한다() {
                Optional<TermsAgreementCheckResult> result =
                        termsAgreementRepository.findAgreementCheckInfo(memberId, termsId, agreedItemIds);

                assertThat(result).isPresent();
                TermsAgreementCheckResult checkResult = result.get();
                // 주어진 약관은 활성화된 약관이어야 한다.
                assertThat(checkResult.isActive()).isTrue();
                // 사용자는 동의한 적이 없어야 한다.
                assertThat(checkResult.hasAgreed()).isFalse();
                // 필수필드를 모두 포함한 동의요청이어야 한다.
                assertThat(checkResult.hasAllRequiredFields()).isTrue();
            }
        }

        @Nested
        class 주어진_약관이_존재하지_않는_경우 {

            long memberId;
            // 존재하지 않는 약관 Id
            final long termsId = 9999L;
            List<Long> agreedItemIds = List.of(1L, 2L, 3L);

            @Test
            void 빈_Optional을_반환해야_한다() {
                Optional<TermsAgreementCheckResult> result =
                        termsAgreementRepository.findAgreementCheckInfo(memberId, termsId, agreedItemIds);

                assertThat(result).isEmpty();
            }
        }


        @Nested
        class 사용자가_이미_주어진_약관에_동의한_경우 {

            long memberId;
            long termsId;
            List<Long> agreedItemIds;

            @BeforeEach
            void setUpContext() {
                MemberEntity member = memberJpaRepository.save(
                        new MemberEntity("김철수", "슈퍼 운영", "chulsoo@naver.com",
                                "profile1.jpg", "oauth_chulsoo", "naver")
                );
                memberId = member.getId();

                TermsEntity terms = termsJpaRepository.save(
                        new TermsEntity("서비스 이용 약관", 1, true)
                );
                termsId = terms.getId();

                TermsItemEntity termsItem1 = termsItemJpaRepository.save(
                        new TermsItemEntity(terms, "개인정보 수집",
                                "서비스 제공 및 운영을 위해 사용자의 개인정보를 수집 및 이용합니다.", TermsType.REQUIRED)
                );

                List<TermsItemEntity> termsItems = List.of(termsItem1);

                TermsAgreementEntity agreement = termsAgreementJpaRepository.save(
                        new TermsAgreementEntity(member, terms, LocalDateTime.now())
                );

                termsItemAgreementJpaRepository.saveAll(
                        termsItems.stream()
                                .map(termsItem -> new TermsItemAgreementEntity(
                                                termsItem,
                                                agreement
                                        )
                                )
                                .toList()
                );

                agreedItemIds = List.of(termsItem1.getId());
            }

            @Test
            void 약관_동의여부_필드는_true여야한다() {
                Optional<TermsAgreementCheckResult> result =
                        termsAgreementRepository.findAgreementCheckInfo(memberId, termsId, agreedItemIds);

                assertThat(result).isPresent();
                TermsAgreementCheckResult checkResult = result.get();
                assertThat(checkResult.isActive()).isTrue();
                assertThat(checkResult.hasAgreed()).isTrue();
                assertThat(checkResult.hasAllRequiredFields()).isTrue();
            }
        }


        @Nested
        class 주어진_약관이_활성화된_약관이_아닐_경우 {

            long memberId;
            long termsId;
            List<Long> agreedItemIds;

            @BeforeEach
            void setUpContext() {
                MemberEntity member = memberJpaRepository.save(
                        new MemberEntity("김철수", "슈퍼 운영", "chulsoo@naver.com",
                                "profile1.jpg", "oauth_chulsoo", "naver")
                );
                memberId = member.getId();

                // 비활성화된 약관
                TermsEntity terms = termsJpaRepository.save(
                        new TermsEntity("서비스 이용 약관", 1, false)
                );
                termsId = terms.getId();

                TermsItemEntity termsItem1 = termsItemJpaRepository.save(
                        new TermsItemEntity(terms, "개인정보 수집",
                                "서비스 제공 및 운영을 위해 사용자의 개인정보를 수집 및 이용합니다.", TermsType.REQUIRED)
                );

                agreedItemIds = List.of(termsItem1.getId());
            }

            @Test
            void 활성화약관_여부_필드는_false여야_한다() {
                Optional<TermsAgreementCheckResult> result =
                        termsAgreementRepository.findAgreementCheckInfo(memberId, termsId, agreedItemIds);

                assertThat(result).isPresent();

                TermsAgreementCheckResult checkResult = result.get();
                assertThat(checkResult.isActive()).isFalse();
                assertThat(checkResult.hasAgreed()).isFalse();
                assertThat(checkResult.hasAllRequiredFields()).isTrue();
            }
        }


        @Nested
        class 사용자의_동의_항목에서_약관_필수_항목중_누락된_항목이_있는_경우 {

            long memberId;
            long termsId;
            List<Long> agreedItemIds;

            @BeforeEach
            void setUpContext() {
                MemberEntity member = memberJpaRepository.save(
                        new MemberEntity("김철수", "슈퍼 운영", "chulsoo@naver.com",
                                "profile1.jpg", "oauth_chulsoo", "naver")
                );
                memberId = member.getId();

                TermsEntity terms = termsJpaRepository.save(
                        new TermsEntity("서비스 이용 약관", 1, true)
                );
                termsId = terms.getId();

                TermsItemEntity termsItem1 = termsItemJpaRepository.save(
                        new TermsItemEntity(terms, "개인정보 수집",
                                "서비스 제공 및 운영을 위해 사용자의 개인정보를 수집 및 이용합니다.", TermsType.REQUIRED)
                );

                TermsItemEntity termsItem2 = termsItemJpaRepository.save(
                        new TermsItemEntity(terms, "서비스 이용 약관 동의",
                                "서비스 이용과 관련된 기본적인 권리 및 의무 사항을 규정합니다.", TermsType.REQUIRED)
                );

                TermsItemEntity termsItem3 = termsItemJpaRepository.save(
                        new TermsItemEntity(terms, "마케팅 활용 동의",
                                "맞춤형 광고 및 프로모션 정보를 제공하기 위해 사용자의 정보를 활용합니다.", TermsType.OPTIONAL)
                );

                // 필수항목 2번에 대한 동의 누락
                agreedItemIds = List.of(termsItem1.getId());
            }

            @Test
            void 필수_항목_동의여부_필드는_false여야_한다() {
                Optional<TermsAgreementCheckResult> result =
                        termsAgreementRepository.findAgreementCheckInfo(memberId, termsId, agreedItemIds);

                assertThat(result).isPresent();

                TermsAgreementCheckResult checkResult = result.get();
                assertThat(checkResult.isActive()).isTrue();
                assertThat(checkResult.hasAgreed()).isFalse();
                assertThat(checkResult.hasAllRequiredFields()).isFalse();
            }
        }
    }
}
