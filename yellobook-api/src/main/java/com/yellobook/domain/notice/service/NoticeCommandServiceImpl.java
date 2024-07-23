package com.yellobook.domain.notice.service;

import com.yellobook.domains.post.repository.AnnounceRepository;
import com.yellobook.domains.post.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeCommandServiceImpl implements NoticeCommandService{
    private final AnnounceRepository announceRepository;
    private final WorkRepository workRepository;

    @Override
    public void deleteNotice(Long teamId, Long noticeId) {
//         삭제 (announce인지, work 인지 판별)
//        announceRepository.delete();
//        workRepository.delete();
    }
}
