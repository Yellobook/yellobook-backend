package com.yellobook.admin.domain;

import com.yellobook.admin.support.error.AdminErrorType;
import com.yellobook.admin.support.error.AdminException;
import com.yellobook.storage.db.core.AdminTermsEntity;
import com.yellobook.storage.db.core.AdminTermsItemEntity;
import com.yellobook.storage.db.core.AdminTermsItemJpaRepository;
import com.yellobook.storage.db.core.AdminTermsJpaRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminTermsService {
    private final AdminTermsJpaRepository adminTermsJpaRepository;
    private final AdminTermsItemJpaRepository adminTermsItemJpaRepository;

    public AdminTermsService(AdminTermsItemJpaRepository adminTermsItemJpaRepository,
                             AdminTermsJpaRepository adminTermsJpaRepository) {
        this.adminTermsItemJpaRepository = adminTermsItemJpaRepository;
        this.adminTermsJpaRepository = adminTermsJpaRepository;
    }

    @Transactional
    public Long createNewTerms(NewTerms terms) {
        int finalizedLatestTermsVersion = adminTermsJpaRepository.findFinalizedLatestTermsVersion()
                .orElse(0);

        AdminTermsEntity newTerms = adminTermsJpaRepository.save(
                new AdminTermsEntity(
                        terms.name(),
                        finalizedLatestTermsVersion + 1
                )
        );

        List<AdminTermsItemEntity> termsItems = terms.termsItems()
                .stream()
                .map(termsItem -> new AdminTermsItemEntity(
                        termsItem.title(),
                        termsItem.content(),
                        termsItem.termsType(),
                        newTerms
                ))
                .toList();
        adminTermsItemJpaRepository.saveAll(termsItems);
        return newTerms.getId();
    }

    @Transactional
    public Long finalizeTerms(Long termsId) {
        AdminTermsEntity terms = adminTermsJpaRepository.findById(termsId)
                .orElseThrow(
                        () -> new AdminException(AdminErrorType.TERMS_NOT_FOUND)
                );
        terms.finalizeTerms();
        return terms.getId();
    }
}
