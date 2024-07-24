package com.yellobook.domain.work.service;

import com.yellobook.domain.auth.security.oauth2.dto.CustomOAuth2User;
import com.yellobook.domains.work.repository.WorkCommentRepository;
import com.yellobook.domains.work.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkCommandService {
    private final WorkRepository workRepository;
    private final WorkCommentRepository workCommentRepository;

    public void deleteWork(Long teamId, Long workId, CustomOAuth2User oAuth2User) {

        //workRepository.deleteById(workId);
    }
}
