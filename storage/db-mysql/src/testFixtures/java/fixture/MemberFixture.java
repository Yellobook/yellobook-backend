package fixture;

import com.yellobook.core.core.enums.MemberRole;
import com.yellobook.core.domains.member.entity.Member;
import java.time.LocalDateTime;
import java.util.UUID;
import support.ReflectionUtil;

public class MemberFixture {
    private static final String MEMBER_NICKNAME = "사용자";
    private static final String MEMBER_PROFILE_IMAGE = "profile.png";
    private static final MemberRole MEMBER_ROLE = MemberRole.USER;
    private static final Boolean MEMBER_ALLOWANCE = true;
    private static final LocalDateTime MEMBER_TIMESTAMP = LocalDateTime.now();


    public static Member createMember() {
        return createMember(MEMBER_NICKNAME, generateUniqueEmail(), MEMBER_PROFILE_IMAGE, MEMBER_ROLE, MEMBER_ALLOWANCE,
                MEMBER_TIMESTAMP);
    }

    public static Member createMember(String nickname) {
        return createMember(nickname, generateUniqueEmail(), MEMBER_PROFILE_IMAGE, MEMBER_ROLE, MEMBER_ALLOWANCE,
                MEMBER_TIMESTAMP);
    }

    public static Member createMember(Boolean allowance) {
        return createMember(MEMBER_NICKNAME, generateUniqueEmail(), MEMBER_PROFILE_IMAGE, MEMBER_ROLE, allowance,
                MEMBER_TIMESTAMP);
    }

    public static Member createMember(String nickname, String email, String profileImage, MemberRole role,
                                      Boolean allowance, LocalDateTime timestamp) {
        Member member = Member.builder()
                .nickname(nickname)
                .email(email)
                .profileImage(profileImage)
                .role(role)
                .allowance(allowance)
                .build();
        ReflectionUtil.setBaseTimeEntityFields(member, timestamp);
        return member;
    }

    private static String generateUniqueEmail() {
        return UUID.randomUUID()
                .toString() + "@gmail.com";
    }
}
