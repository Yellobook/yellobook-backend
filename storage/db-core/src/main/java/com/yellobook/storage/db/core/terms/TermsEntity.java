package com.yellobook.storage.db.core.terms;

import com.yellobook.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "terms")
public class TermsEntity extends BaseEntity {
    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    Integer version;

    @Column(nullable = false)
    Boolean isActive;

    @OneToMany(mappedBy = "terms")
    private List<TermsItemEntity> termsItems = new ArrayList<>();

    protected TermsEntity() {
    }
}
