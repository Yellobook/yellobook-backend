package com.yellobook.domain.announce.service;

import com.yellobook.domain.announce.dto.AnnounceRequest;
import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.announce.entity.Announce;

public interface AnnounceCommandService {
    Announce createAnnounce(Long memberId, AnnounceRequest.PostAnnounceRequestDTO request);
    void deleteAnnounce(Long memberId, CustomOAuth2User oauth2User);
}
