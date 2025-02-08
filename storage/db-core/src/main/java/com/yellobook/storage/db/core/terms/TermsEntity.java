package com.yellobook.storage.db.core.terms;

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
