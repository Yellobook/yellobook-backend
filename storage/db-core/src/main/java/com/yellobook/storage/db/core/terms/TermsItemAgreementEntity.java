package com.yellobook.storage.db.core.terms;

import com.yellobook.storage.db.core.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "terms_item_agreements")
public class TermsItemAgreementEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_item_id", nullable = false)
    private TermsItemEntity termsItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_agreement_id", nullable = false)
    private TermsAgreementEntity termsAgreement;

    protected TermsItemAgreementEntity() {
    }

    public TermsItemAgreementEntity(TermsItemEntity termsItem, TermsAgreementEntity termsAgreement) {
        this.termsItem = termsItem;
        this.termsAgreement = termsAgreement;
    }
}
