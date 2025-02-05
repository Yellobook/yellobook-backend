package fixture;

import com.yellobook.inform.InformEntity;
import com.yellobook.inform.InformMention;
import com.yellobook.member.Member;
import com.yellobook.team.Team;
import java.time.LocalDate;
import java.time.LocalDateTime;
import support.ReflectionUtil;

public class InformFixture {
    private static final String INFORM_TITLE = "공지";
    private static final String INFORM_CONTENT = "내용";
    private static final LocalDate INFORM_DATE = LocalDate.now();
    private static final LocalDateTime INFORM_TIMESTAMP = LocalDateTime.now();

    public static InformEntity createInform(Team team, Member member) {
        return createInform(team, member, INFORM_DATE, INFORM_TITLE, INFORM_CONTENT, INFORM_TIMESTAMP);
    }

    public static InformEntity createInform(Team team, Member member, LocalDate date) {
        return createInform(team, member, date, INFORM_TITLE, INFORM_CONTENT, INFORM_TIMESTAMP);
    }

    public static InformEntity createInform(Team team, Member member, LocalDate date, String title, String content,
                                            LocalDateTime timestamp) {
        InformEntity inform = InformEntity.builder()
                .title(title)
                .content(content)
                .date(date)
                .team(team)
                .member(member)
                .build();
        ReflectionUtil.setBaseTimeEntityFields(inform, timestamp);
        return inform;
    }

    public static InformMention createInformMention(InformEntity inform, Member member) {
        return InformMention.builder()
                .inform(inform)
                .member(member)
                .build();
    }
}
