package com.yellobook.storage.db.core.terms;

import com.yellobook.storage.db.core.BaseEntity;
import com.yellobook.storage.db.core.member.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "terms_agreements")
public class TermsAgreementEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_id")
    private TermsEntity termsEntity;

    @Column(nullable = false)
    LocalDateTime agreedAt;

    public TermsAgreementEntity(MemberEntity memberEntity, TermsEntity termsEntity, LocalDateTime agreedAt) {
        this.memberEntity = memberEntity;
        this.termsEntity = termsEntity;
        this.agreedAt = agreedAt;
    }

    protected TermsAgreementEntity() {
    }

    public MemberEntity getMemberEntity() {
        return memberEntity;
    }

    public TermsEntity getTermsEntity() {
        return termsEntity;
    }

    public LocalDateTime getAgreedAt() {
        return agreedAt;
    }
}
