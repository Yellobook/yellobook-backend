package com.yellobook.core.domain.member;

import com.yellobook.core.error.CoreErrorType;
import com.yellobook.core.error.CoreException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberReader memberReader;
    private final JoinedTeamReader joinedTeamReader;
    private final MemberWriter memberWriter;
    private final ProfileEditor profileEditor;

    public MemberService(MemberReader memberReader, JoinedTeamReader joinedTeamReader, MemberWriter memberWriter,
                         ProfileEditor profileEditor) {
        this.memberReader = memberReader;
        this.joinedTeamReader = joinedTeamReader;
        this.memberWriter = memberWriter;
        this.profileEditor = profileEditor;
    }

    public Long getIdOrRegister(ProfileInfo profileInfo, SocialInfo socialInfo) {
        return memberReader.read(socialInfo)
                .map(Member::memberId)
                .orElseGet(() -> memberWriter.add(profileInfo, socialInfo));
    }

    public Member read(Long memberId) {
        return memberReader.read(memberId);
    }

    public void updateNickname(Member member, String newNickname) {
        if (!profileEditor.canChangeNickname(member)) {
            throw new CoreException(CoreErrorType.NICKNAME_CHANGE_NOT_ALLOWED);
        }
        profileEditor.updateNickname(member, newNickname);
    }

    public void updateBio(Member member, String newBio) {
        profileEditor.updateBio(member, newBio);
    }

    public void deleteMember(Member member) {
        memberWriter.delete(member);
    }

    public List<JoinedTeamResult> getMemberJoinedTeams(Member member) {
        return joinedTeamReader.read(member);
    }
}



