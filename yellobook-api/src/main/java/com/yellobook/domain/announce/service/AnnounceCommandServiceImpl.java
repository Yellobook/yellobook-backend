package com.yellobook.domain.announce.service;

import com.yellobook.domain.announce.dto.AnnounceRequest;
import com.yellobook.domains.announce.entity.Announce;
import com.yellobook.domains.announce.repository.AnnounceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AnnounceCommandServiceImpl implements AnnounceCommandService {
    private final AnnounceRepository announceRepository;

    @Override
    public Announce createAnnounce(Long teamId, Long memberId, AnnounceRequest.PostAnnounceRequestDTO request){
        return null;
    }
}
