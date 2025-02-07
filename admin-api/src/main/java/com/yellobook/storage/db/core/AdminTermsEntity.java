package com.yellobook.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "terms")
public class AdminTermsEntity extends AdminBaseEntity {
    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    Integer version;

    @Column(nullable = false)
    Boolean isActive;

    @Column(nullable = false)
    Boolean isFinalized;

    @Column
    LocalDate effectiveDate;

    protected AdminTermsEntity() {
    }

    public AdminTermsEntity(String name, Integer version) {
        this.name = name;
        this.version = version;
        this.isActive = false;
        this.isFinalized = false;
        this.effectiveDate = null;
    }

    public void finalizeTerms() {
        this.isFinalized = true;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void deactivate() {
        this.isActive = false;
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