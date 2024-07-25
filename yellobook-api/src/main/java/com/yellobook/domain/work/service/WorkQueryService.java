package com.yellobook.domain.work.service;


import com.yellobook.domains.work.entity.Work;

public interface WorkQueryService {
    Work getWorkById(Long workId);
}
