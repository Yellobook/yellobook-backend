package com.yellobook.domain.work.service;

import com.yellobook.domain.work.dto.WorkRequest;
import com.yellobook.domains.work.entity.Work;
import com.yellobook.domains.work.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkCommandServiceImpl implements WorkCommandService {
    private final WorkRepository workRepository;
    @Override
    public Work createWork(Long teamId, Long memberId, WorkRequest.PostWorkRequestDTO request){
        return null;
    }
}
