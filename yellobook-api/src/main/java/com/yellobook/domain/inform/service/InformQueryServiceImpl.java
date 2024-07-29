package com.yellobook.domain.inform.service;

import com.yellobook.domains.inform.entity.Inform;
import com.yellobook.domains.inform.entity.InformComment;
import com.yellobook.domains.inform.repository.InformCommentRepository;
import com.yellobook.domains.inform.repository.InformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InformQueryServiceImpl implements InformQueryService {
    private final InformRepository informRepository;
    private final InformCommentRepository informCommentRepository;

    @Override
    public Inform getInformById(Long informId){
        return null;
    }

    @Override
    public InformComment getCommentByInformId(Long informId){
        return null;
    }

    @Override
    public boolean existInformById(Long informId) {
        return informRepository.existsById(informId);
    }
}
