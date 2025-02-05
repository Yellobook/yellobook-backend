package com.yellobook.core.domain.member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository {
    Long save(NewMember newMember);

    boolean existById(Long memberId);

    Optional<Member> findById(Long memberId);

    Optional<Member> findBySocialInfo(SocialInfo socialInfo);

    void delete(Member member);

    LocalDateTime getNicknameUpdatedAt(Member member);

    void updateNickname(Member member, String newNickname);

    void updateBio(Member member, String newBio);

    List<JoinedTeamResult> findJoinedTeamsByMemberId(Member member);
}
