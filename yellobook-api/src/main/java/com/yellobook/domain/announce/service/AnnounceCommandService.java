package com.yellobook.domain.announce.service;

import com.yellobook.domain.announce.dto.AnnounceRequest;
import com.yellobook.domains.announce.entity.Announce;

public interface AnnounceCommandService {
    Announce createAnnounce(Long memberId, AnnounceRequest.PostAnnounceRequestDTO request);
    Boolean deleteAnnounce(Long memberId, Long announceId);
}
