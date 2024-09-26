package fixture;

import com.yellobook.common.enums.MemberTeamRole;
import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import java.time.LocalDateTime;
import support.ReflectionUtils;

public class TeamFixture {
    private static final String TEAM_NAME = "팀1";
    private static final String TEAM_PHONE_NUMBER = "010-1234-5678";
    private static final String TEAM_ADDRESS = "서울특별시";
    private static final LocalDateTime TEAM_TIMESTAMP = LocalDateTime.now();

    public static Team createTeam() {
        return createTeam(TEAM_NAME, TEAM_PHONE_NUMBER, TEAM_ADDRESS, TEAM_TIMESTAMP);
    }

    public static Team createTeam(String name, String phoneNumber, String address, LocalDateTime timestamp) {
        Team team = Team.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .address(address)
                .build();
        ReflectionUtils.setBaseTimeEntityFields(team, timestamp);
        return team;
    }

    public static Participant createParticipant(Team team, Member member, MemberTeamRole role) {
        return Participant.builder()
                .team(team)
                .member(member)
                .role(role)
                .build();
    }
}
