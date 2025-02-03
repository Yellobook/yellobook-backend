package com.yellobook.core.domain.schedule;

import com.yellobook.core.domain.member.Member;
import com.yellobook.core.domain.team.Team;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;


@Service
public class ScheduleService {
    private final ScheduleReader scheduleReader;
    private final ScheduleViewProcessor scheduleViewProcessor;
    private final ScheduleAccessManager scheduleAccessManager;
    private final ScheduleWriter scheduleWriter;
    private final ScheduleMentionProcessor scheduleMentionProcessor;
    private static final Logger logger = Logger.getLogger(ScheduleService.class.getName());

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
    public Long create(String title, String content, LocalDate plannedDate, Member author,
                       List<Long> mentionedMemberIds, Team team) {
        Long scheduleId = scheduleWriter.create(
                new NewSchedule(title, content, plannedDate, author, mentionedMemberIds, team));
        scheduleMentionProcessor.mention(scheduleId, mentionedMemberIds);
        return scheduleId;
    }

    // Infom 도메인에 필요한 객체는
    // Author, MentionedMember
    // 조회로직에는 Author 인지, MentioendMember 인지 모름 ...
    // 그래서, AuthorOrMentionedMember
    // 변환이 ..... 엄청마
    // Member 객체를 아예 이용하는게 좋을까?

    // 그러면, Author 는 왜 ?? 필요한가?
    // 어차피 Member 로 퉁칠꺼면 Author 는 조회전용이 되는거 아닌가?

    // 작성자 "만"
    @Transactional
    public void delete(Long scheduleId, Member member) {
        scheduleAccessManager.isAuthor(scheduleId, member);
        scheduleWriter.delete(scheduleId);
    }

    // 작성자 + 언급된 사용자
    @Transactional
    public void increaseView(Long informId, Member member) {
        scheduleAccessManager.isAuthorOrMentioned(informId, member.memberId());
        scheduleViewProcessor.increase(informId);
    }

    public Schedule read(Long informId, Member member) {
        scheduleAccessManager.isAuthorOrMentioned(informId, member.memberId());
        return scheduleReader.read(informId);
    }
}