package fixture;

import com.yellobook.domains.member.entity.Member;
import com.yellobook.domains.team.entity.Participant;
import com.yellobook.domains.team.entity.Team;
import com.yellobook.TeamMemberRole;
import java.time.LocalDateTime;
import support.ReflectionUtil;

public class TeamFixture {
    private static final String TEAM_PHONE_NUMBER = "010-1234-5678";
    private static final String TEAM_ADDRESS = "서울특별시";
    private static final LocalDateTime TEAM_TIMESTAMP = LocalDateTime.now();

    public static Team createTeam(String teamName) {
        return createTeam(teamName, TEAM_PHONE_NUMBER, TEAM_ADDRESS, TEAM_TIMESTAMP);
    }

    public static Team createTeam(String teamName, String phoneNumber, String address, LocalDateTime timestamp) {
        Team team = Team.builder()
                .name(teamName)
                .phoneNumber(phoneNumber)
                .address(address)
                .build();
        ReflectionUtil.setBaseTimeEntityFields(team, timestamp);
        return team;
    }

    public static Participant createParticipant(Team team, Member member, TeamMemberRole teamMemberRole) {
        return Participant.builder()
                .team(team)
                .member(member)
                .teamMemberRole(teamMemberRole)
                .build();
    }
}
