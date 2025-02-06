package com.yellobook.storage.db.core.terms;

import static org.assertj.core.api.Assertions.assertThat;

import com.yellobook.core.domain.terms.Terms;
import com.yellobook.core.domain.terms.Terms.TermsItem;
import com.yellobook.core.enums.TermsType;
import com.yellobook.storage.db.support.RepositoryTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TermsCoreRepositoryTest extends RepositoryTest {
    @Autowired
    TermsCoreRepository termsCoreRepository;

    @Autowired
    TermsJpaRepository termsJpaRepository;

    @Autowired
    TermsItemJpaRepository termsItemJpaRepository;

    @Nested
    class findActiveTerms_메서드는 {

        @Nested
        class 활성화된_약관이_존재할_경우 {
            long givenTermsId;
            List<TermsItemEntity> givenTermsItems;

            @BeforeEach
            void setUpContext() {
                TermsEntity terms = termsJpaRepository.save(
                        new TermsEntity("서비스 이용 약관", 1, true)
                );
                givenTermsId = terms.getId();
                givenTermsItems = List.of(
                        termsItemJpaRepository.save(
                                new TermsItemEntity(terms, "개인정보 수집",
                                        "서비스 제공 및 운영을 위해 사용자의 개인정보를 수집 및 이용합니다.", TermsType.REQUIRED)
                        ),
                        termsItemJpaRepository.save(
                                new TermsItemEntity(terms, "마케팅 활용 동의",
                                        "맞춤형 광고 및 프로모션 정보를 제공하기 위해 사용자의 정보를 활용합니다.", TermsType.OPTIONAL)
                        ));
            }

            @Test
            void 약관항목을_포함한_약관을_반환한다() {
                Optional<Terms> result = termsCoreRepository.findActiveTerms();

                assertThat(result).isPresent();
                Terms terms = result.get();
                List<TermsItem> termsItems = terms.termsItems();

                assertThat(terms.termsId()).isEqualTo(givenTermsId);
                assertThat(termsItems.size()).isEqualTo(givenTermsItems.size());

                assertThat(termsItems)
                        .usingRecursiveAssertion()
                        .isEqualTo(givenTermsItems.stream()
                                .map(TermsItemEntity::toTermsItem)
                                .toList());
            }
        }

        @Nested
        class 활성화된_약관이_존재하지_않을_경우 {
            @BeforeEach
            void setUpContext() {
                termsJpaRepository.save(
                        new TermsEntity("서비스 이용 약관", 1, false)
                );
            }

            @Test
            void 빈_Optional을_반환한다() {
                Optional<Terms> result = termsCoreRepository.findActiveTerms();
                assertThat(result).isEmpty();
            }
        }
    }
}