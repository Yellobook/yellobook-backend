package com.yellobook.core.domain.team;

import com.yellobook.core.domain.common.TeamMemberRole;
import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamApplyManager {
    private final TeamCachedRepository teamCachedRepository;
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Autowired
    public TeamApplyManager(TeamCachedRepository teamRedisRepository) {
        this.teamCachedRepository = teamRedisRepository;
    }

    /*
    초대 url 생성
     */
    public String generateInvitationCode(Long teamId) {
        String code = generateNanoId();
//                UUID.randomUUID().toString();
        String key = generateInvitationKey(code);
        String value = teamId.toString();
        // 15분 간 유효한 링크 설정
        long validTime = 15;
        teamCachedRepository.saveInvitationCode(key, value, validTime, TimeUnit.MINUTES);
        return code;
    }

    private String generateNanoId() {
        int length = 10;
        SecureRandom random = new SecureRandom();
        StringBuilder nanoId = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            nanoId.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return nanoId.toString();
    }

    /*
    초대 정보 확인
     */
    public Long read(String code) {
        String value = teamCachedRepository.readTeamIdByCode("team:invite:" + code);
        if (value == null) {
            throw new CoreException(CoreErrorType.INVITATION_NOT_FOUND);
        }
        return Long.parseLong(value);
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
    private String generateInvitationKey(String code) {
        return "team:invite:" + code;
    }

    public void requestTeamJoin(Long teamId, Long memberId) {
        String key = generateApplyTeamKey(teamId);
        teamCachedRepository.applyTeam(key, memberId);
    }

    public void deleteJoinRequest(Long teamId, Long memberId) {
        String key = generateApplyTeamKey(teamId);
        if (teamCachedRepository.isTeamJoinRequestExist(key, memberId)) {
            throw new CoreException(CoreErrorType.APPLY_TEAM_NOT_FOUND);
        }
        teamCachedRepository.removeTeamJoinRequest(key, memberId);
    }

    private String generateApplyTeamKey(Long teamId) {
        return "team:apply:" + teamId;
    }


}
