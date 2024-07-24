package com.yellobook.domain.announce.service;

import com.yellobook.domain.announce.dto.AnnounceRequest;
import com.yellobook.domains.announce.entity.Announce;

public interface AnnounceCommandService {
    Announce createAnnounce(Long teamId, Long memberId, AnnounceRequest.PostAnnounceRequestDTO request);
}
