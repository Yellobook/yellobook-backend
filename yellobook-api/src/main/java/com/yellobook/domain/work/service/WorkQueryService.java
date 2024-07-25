package com.yellobook.domain.work.service;


import com.yellobook.domains.work.entity.Work;
import com.yellobook.domains.work.entity.WorkComment;


public interface WorkQueryService {
    Work getWorkById(Long workId);
    WorkComment getWorkCommentById(Long workId);
}
