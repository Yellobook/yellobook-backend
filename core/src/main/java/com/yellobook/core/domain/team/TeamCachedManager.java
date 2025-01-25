package com.yellobook.core.domain.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.domain.team.dto.InvitationCodeInfo;
import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TeamCachedManager {
    @Value("${backend.base-url}")
    private String baseUrl;

    private final TeamCachedRepository teamCachedRepository;

    public TeamCachedManager(TeamCachedRepository teamRedisRepository) {
        this.teamCachedRepository = teamRedisRepository;
    }

    /*
    초대 url 생성
     */
    public String generateInvitationUrl(Long teamId, TeamMemberRole role) {
        String code = UUID.randomUUID()
                .toString();
        String key = generateInvitaionKey(code);
        String value = teamId + ":" + role;
        // 15분 간 유효한 링크 설정
        long validTime = 15;
        teamCachedRepository.save(key, value, validTime, TimeUnit.MINUTES);
        String invitationUrl = baseUrl;
        return buildUrl(invitationUrl, code);
    }

    private String buildUrl(String invitationUrl, String code) {
        return invitationUrl + "/api/v1/invitation?code=" + code;
    }

    /*
    초대 정보 확인
     */
    public InvitationCodeInfo read(String key) {
        Object value = teamCachedRepository.read("team:invite:" + key);
        if (value == null) {
            throw new CoreException(CoreErrorType.INVITATION_NOT_FOUND);
        }

        // value 형식이 "teamId:role" 인지 확인하고 분리
        String stringValue = value.toString();
        String[] parts = stringValue.split(":");
        int validLength = 2;

        if (parts.length != validLength) {
            throw new CoreException(CoreErrorType.INVALID_INVITATION);
        }

        Long teamId = Long.parseLong(parts[0]);
        TeamMemberRole role = TeamMemberRole.valueOf(parts[1]);

        return new InvitationCodeInfo(teamId, role);
    }

    /*
    현재 팀 설정
     */
    public void setMemberCurrentTeam(Long teamId, Long memberId, TeamMemberRole role) {
        teamCachedRepository.saveCurrentTeam(teamId, memberId, role.getDescription());
    }

    /*
    초대 키 발행
     */
    private String generateInvitaionKey(String code) {
        return "team:invite:" + code;
    }

    /*
    현재 팀 정보 가져오기
     */
//    public Long getMemberCurrentTeamId(Long teamId) {
//    }
}
