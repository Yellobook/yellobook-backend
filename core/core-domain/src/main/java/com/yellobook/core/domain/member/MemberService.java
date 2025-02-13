package com.yellobook.core.domain.member;

import com.yellobook.core.support.error.CoreErrorType;
import com.yellobook.core.support.error.CoreException;
import java.util.List;
import java.util.Optional;
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

    public boolean existMemberByEmail(String email) {
        return memberReader.exist(email);
    }

    public Optional<Member> findByEmail(String email) {
        return memberReader.find(email);
    }

    public Long create(ProfileInfo profileInfo, SocialInfo socialInfo) {
        return memberWriter.add(profileInfo, socialInfo);
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

    public List<JoinedTeamResult> getMemberJoinedStores(Member member) {
        return joinedTeamReader.read(member);
    }
}



