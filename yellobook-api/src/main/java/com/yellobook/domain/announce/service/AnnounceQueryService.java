package com.yellobook.domain.announce.service;

import com.yellobook.domains.announce.entity.Announce;
import com.yellobook.domains.announce.entity.AnnounceComment;

public interface AnnounceQueryService {
    Announce findById(Long announceId);
    AnnounceComment findCommentById(Long announceId);
}
