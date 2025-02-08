package com.yellobook.core.domain.member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Long save(NewMember newMember);

    boolean existByEmail(String email);

    Optional<Member> findById(Long memberId);

    Optional<Member> findBySocialInfo(SocialInfo socialInfo);

    Optional<Member> findByEmail(String email);

    void delete(Member member);

    LocalDateTime getNicknameUpdatedAt(Member member);

    void updateNickname(Member member, String newNickname);

    void updateBio(Member member, String newBio);

    List<JoinedTeamResult> findJoinedTeamsByMemberId(Long memberId);
}
