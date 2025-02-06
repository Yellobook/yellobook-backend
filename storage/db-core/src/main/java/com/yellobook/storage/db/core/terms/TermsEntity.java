package com.yellobook.storage.db.core.terms;

import com.yellobook.core.domain.terms.Terms;
import com.yellobook.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "terms")
public class TermsEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    private Boolean isActive;

    protected TermsEntity() {
    }

    public static Terms toTerms(TermsEntity terms) {
        return new Terms(
                terms.id,
                terms.name,
                terms.version,
                null
        );
    }

    public TermsEntity(String name, Integer version, Boolean isActive) {
        this.name = name;
        this.version = version;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public Integer getVersion() {
        return version;
    }

    public Boolean getActive() {
        return isActive;
    }
}
