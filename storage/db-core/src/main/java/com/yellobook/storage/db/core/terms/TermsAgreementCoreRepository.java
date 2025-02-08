package com.yellobook.storage.db.core.terms;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yellobook.core.domain.terms.TermsAgreement;
import com.yellobook.core.domain.terms.TermsAgreementCheckResult;
import com.yellobook.core.domain.terms.TermsAgreementRepository;
import com.yellobook.core.enums.TermsType;
import com.yellobook.storage.db.core.member.MemberEntity;
import com.yellobook.storage.db.core.member.MemberJpaRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TermsAgreementCoreRepository implements TermsAgreementRepository {
    private final JPAQueryFactory queryFactory;
    private final TermsAgreementJpaRepository termsAgreementJpaRepository;
    private final TermsItemJpaRepository termsItemJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final TermsJpaRepository termsJpaRepository;
    private final TermsItemAgreementJpaRepository termsItemAgreementJpaRepository;

    public TermsAgreementCoreRepository(JPAQueryFactory queryFactory,
                                        TermsAgreementJpaRepository termsAgreementJpaRepository,
                                        MemberJpaRepository memberJpaRepository,
                                        TermsJpaRepository termsJpaRepository,
                                        TermsItemJpaRepository termsItemJpaRepository,
                                        TermsItemAgreementJpaRepository termsItemAgreementJpaRepository
    ) {
        this.queryFactory = queryFactory;
        this.termsAgreementJpaRepository = termsAgreementJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.termsJpaRepository = termsJpaRepository;
        this.termsItemJpaRepository = termsItemJpaRepository;
        this.termsItemAgreementJpaRepository = termsItemAgreementJpaRepository;
    }

    @Override
    public boolean hasMemberAgreedToActiveTerms(long memberId) {
        QTermsEntity terms = QTermsEntity.termsEntity;
        QTermsAgreementEntity termsAgreement = QTermsAgreementEntity.termsAgreementEntity;
        return queryFactory.selectOne()
                .from(terms)
                .join(termsAgreement)
                .on(termsAgreement.termsEntity.id.eq(terms.id))
                .where(terms.isActive.isTrue()
                        .and(termsAgreement.memberEntity.id.eq(memberId)))
                .fetchFirst() != null;
    }

    @Override
    @Transactional
    public TermsAgreement agreeToActiveTerms(long memberId, long termsId,
                                             List<Long> termsItemIds) {
        MemberEntity member = memberJpaRepository.getReferenceById(memberId);
        TermsEntity terms = termsJpaRepository.getReferenceById(termsId);
        LocalDateTime termsAgreeAt = LocalDateTime.now();

        TermsAgreementEntity termsAgreement = termsAgreementJpaRepository.save(
                new TermsAgreementEntity(member, terms, termsAgreeAt));

        List<TermsItemAgreementEntity> termsItemAgreements = termsItemIds.stream()
                .map(termsItemId -> new TermsItemAgreementEntity(
                        termsItemJpaRepository.getReferenceById(termsItemId),
                        termsAgreement
                ))
                .toList();

        termsItemAgreementJpaRepository.saveAll(termsItemAgreements);

        return new TermsAgreement(
                termsAgreement.getId(),
                memberId,
                termsId,
                termsAgreeAt
        );
    }

    @Override
    public Optional<TermsAgreementCheckResult> findAgreementCheckInfo(long memberId, long termsId,
                                                                      List<Long> termsItemIds) {
        QTermsEntity terms = QTermsEntity.termsEntity;
        QTermsAgreementEntity agreement = QTermsAgreementEntity.termsAgreementEntity;
        QTermsItemEntity termsItem = QTermsItemEntity.termsItemEntity;

        BooleanExpression hasAgreedToTerms = JPAExpressions
                .selectFrom(agreement)
                .where(agreement.memberEntity.id.eq(memberId)
                        .and(agreement.termsEntity.id.eq(termsId)))
                .exists();

        BooleanExpression hasAllRequiredTerms = JPAExpressions
                .selectFrom(termsItem)
                .where(termsItem.terms.id.eq(termsId)
                        .and(termsItem.type.eq(TermsType.REQUIRED))
                        .and(termsItem.id.notIn(termsItemIds)))
                .notExists();

        return Optional.ofNullable(queryFactory
                        .select(new QTermsAgreementCheckDto(
                                terms.id,
                                terms.isActive,
                                hasAgreedToTerms,
                                hasAllRequiredTerms))
                        .from(terms)
                        .where(terms.id.eq(termsId))
                        .fetchOne())
                .map(TermsAgreementCheckDto::toTermsAgreementCheckResult);
    }
}
