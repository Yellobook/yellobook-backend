package com.yellobook.domain.announce.service;

import com.yellobook.domains.announce.entity.Announce;

public interface AnnounceQueryService {
    Announce findById(Long teamId, Long announceId);
}
