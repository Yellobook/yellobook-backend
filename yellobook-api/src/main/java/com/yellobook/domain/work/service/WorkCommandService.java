package com.yellobook.domain.work.service;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.work.dto.WorkRequest;
import com.yellobook.domains.work.entity.Work;

public interface WorkCommandService {
    Work createWork(Long memberId, WorkRequest.CreateWorkRequestDTO request);
    void deleteWork(Long workId, CustomOAuth2User oAuth2User);
}
