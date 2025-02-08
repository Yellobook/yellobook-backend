package com.yellobook.core.domain.schedule;

import com.yellobook.core.domain.member.Member;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class ScheduleService {
    private final ScheduleReader scheduleReader;
    private final ScheduleViewProcessor scheduleViewProcessor;
    private final ScheduleAccessManager scheduleAccessManager;
    private final ScheduleWriter scheduleWriter;
    private final ScheduleMentionProcessor scheduleMentionProcessor;

    public ScheduleService(ScheduleReader scheduleReader,
                           ScheduleViewProcessor scheduleViewProcessor, ScheduleAccessManager scheduleAccessManager,
                           ScheduleWriter scheduleWriter, ScheduleMentionProcessor scheduleMentionProcessor) {
        this.scheduleReader = scheduleReader;
        this.scheduleViewProcessor = scheduleViewProcessor;
        this.scheduleAccessManager = scheduleAccessManager;
        this.scheduleWriter = scheduleWriter;
        this.scheduleMentionProcessor = scheduleMentionProcessor;
    }

    @Transactional
    public Long create(NewSchedule newSchedule) {
        Long scheduleId = scheduleWriter.create(newSchedule);
        scheduleMentionProcessor.mention(scheduleId, newSchedule.memberIds());
        return scheduleId;
    }

    // 작성자 "만"
    @Transactional
    public Long delete(Long scheduleId, Member member) {
        scheduleAccessManager.isAuthor(scheduleId, member);
        scheduleWriter.delete(scheduleId);
        return scheduleId;
    }

    // 작성자 + 언급된 사용자
    public Schedule read(Long scheduleId, Member member) {
        Schedule schedule = scheduleReader.read(scheduleId);
        scheduleAccessManager.isAuthorOrMentioned(schedule, member);
        increaseView(scheduleId);
    }

    public List<ScheduleMention> getMentions(Schedule schedule) {
        return scheduleMentionProcessor.getMentions(schedule.scheduleId());
    }

    public void increaseView(Long scheduleId) {
        scheduleViewProcessor.increase(scheduleId);
    }
}