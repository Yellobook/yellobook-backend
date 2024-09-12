package fixture;

import com.yellobook.common.enums.MemberRole;
import com.yellobook.domains.member.entity.Member;
import support.ReflectionUtils;

import java.time.LocalDateTime;
import java.util.UUID;

public class MemberFixture {
    private static final String MEMBER_NICKNAME = "사용자";
    private static final String MEMBER_PROFILE_IMAGE = "profile.png";
    private static final MemberRole MEMBER_ROLE = MemberRole.USER;
    private static final Boolean MEMBER_ALLOWANCE = true;
    private static final LocalDateTime MEMBER_TIMESTAMP = LocalDateTime.now();


    public static Member createMember() {
        LocalDateTime now = LocalDateTime.now();
        return createMember(MEMBER_NICKNAME, generateUniqueEmail(), MEMBER_PROFILE_IMAGE, MEMBER_ROLE, MEMBER_ALLOWANCE, MEMBER_TIMESTAMP);
    }

    public static Member createMember(Boolean allowance) {
        LocalDateTime now = LocalDateTime.now();
        return createMember(MEMBER_NICKNAME, generateUniqueEmail(), MEMBER_PROFILE_IMAGE, MEMBER_ROLE, allowance, MEMBER_TIMESTAMP);
    }

    public static Member createMember(String nickname, String email, String profileImage, MemberRole role, Boolean allowance, LocalDateTime timestamp) {
        Member member = Member.builder()
                .nickname(nickname)
                .email(email)
                .profileImage(profileImage)
                .role(role)
                .allowance(allowance)
                .build();
        ReflectionUtils.setBaseTimeEntityFields(member, timestamp);
        return member;
    }

    private static String generateUniqueEmail() {
        return UUID.randomUUID().toString() + "@gmail.com";
    }
}
