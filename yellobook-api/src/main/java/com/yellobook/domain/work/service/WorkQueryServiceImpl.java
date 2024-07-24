package com.yellobook.domain.work.service;

import com.yellobook.domains.work.repository.WorkCommentRepository;
import com.yellobook.domains.work.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkQueryServiceImpl {
    private final WorkRepository workRepository;
    private final WorkCommentRepository workCommentRepository;
}
