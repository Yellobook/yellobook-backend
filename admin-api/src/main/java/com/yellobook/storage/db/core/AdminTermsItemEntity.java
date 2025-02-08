package com.yellobook.storage.db.core;

import com.yellobook.core.enums.TermsType;
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
public class AdminTermsItemEntity extends AdminBaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private TermsType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_id")
    private AdminTermsEntity terms;

    protected AdminTermsItemEntity() {
    }

    public AdminTermsItemEntity(String title, String content, TermsType type, AdminTermsEntity terms) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.terms = terms;
    }
}
