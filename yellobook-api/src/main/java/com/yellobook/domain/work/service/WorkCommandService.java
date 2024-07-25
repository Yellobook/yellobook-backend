package com.yellobook.domain.work.service;

import com.yellobook.domain.work.dto.WorkRequest;
import com.yellobook.domains.work.entity.Work;

public interface WorkCommandService {
    Work createWork(Long memberId, WorkRequest.CreateWorkRequestDTO request);
    Boolean deleteWork(Long memberId, Long workId);
}
