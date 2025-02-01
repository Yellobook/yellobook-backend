package com.yellobook.core.domain.terms;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsRepository {
    // 약관 생성 (관리자)
    Long save(Terms terms);

    // 사용자가 약관에 동의
    void agree(TermsAgreement agreement);

    // 필수 동의 항목 조회
    List<Long> findRequiredTermsItemId(Terms terms);

    // 활성화된 약관 조회
    Optional<Terms> findActiveTerms();

    // 약관 동의여부 확인
    boolean hasMemberAgreedToActiveTerms(Long memberId);

    Optional<Terms> findTermsById(Long id);
}
