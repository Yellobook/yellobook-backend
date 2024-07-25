package com.yellobook.domain.announce.service;

import com.yellobook.domains.announce.entity.Announce;
import com.yellobook.domains.announce.entity.AnnounceComment;
import com.yellobook.domains.announce.repository.AnnounceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnnounceQueryServiceImpl implements AnnounceQueryService {
    private final AnnounceRepository announceRepository;

    @Override
    public Announce findById(Long announceId){
        return null;
    }

    @Override
    public AnnounceComment findCommentById(Long announceId){
        return null;
    }
}
