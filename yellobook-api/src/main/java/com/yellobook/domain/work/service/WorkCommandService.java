package com.yellobook.domain.work.service;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domain.work.dto.WorkCommentRequest;
import com.yellobook.domain.work.dto.WorkRequest;
import com.yellobook.domains.work.entity.Work;
import com.yellobook.domains.work.entity.WorkComment;

public interface WorkCommandService {
    Work createWork(Long memberId, WorkRequest.CreateWorkRequestDTO request);
    void deleteWork(Long workId, CustomOAuth2User oAuth2User);
    WorkComment createComment(Long memberId, CustomOAuth2User oauth2User, WorkCommentRequest.PostCommentRequestDTO request);
}
