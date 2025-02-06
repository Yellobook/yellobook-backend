package com.yellobook.storage.db.core.terms;

import com.yellobook.core.domain.terms.Terms.TermsItem;
import com.yellobook.core.enums.TermsType;
import com.yellobook.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "terms_items")
public class TermsItemEntity extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private TermsType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_id")
    private TermsEntity terms;

    protected TermsItemEntity() {
    }

    public TermsItemEntity(TermsEntity terms, String title, String content, TermsType type) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.terms = terms;
    }

    public static TermsItem toTermsItem(TermsItemEntity termsItem) {
        return new TermsItem(
                termsItem.getId(),
                termsItem.getTitle(),
                termsItem.getContent(),
                termsItem.getType()
        );
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public TermsType getType() {
        return type;
    }

    public TermsEntity getTerms() {
        return terms;
    }
}