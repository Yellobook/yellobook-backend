package com.yellobook.core.domain.member;

import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MemberReader {
    private final MemberRepository memberRepository;

    public MemberReader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 비즈니스 요구사항에서 값이 분명히 있다고 한다면 throw 를 여기서 하는게 맞을 텐데
    // 그러지 않고 진짜 Optional 에 대한 처리가 비즈니스적으로 필요한 경우가 생길 수 있다.
    // 예외처리가 Reader 랑 Service 에 둘다 존재할 수 있게 되는데
    // -> 허용을 할지 -> Service 에서 몰아서 할지
    public Member read(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException());
    }

    public Optional<Member> read(String email) {
        return memberRepository.findByEmail(email);
    }

    // null 을 주던 Optional 을 주던 해야함 -> 있을수도 있고 없을 수도 있는

//    Member member = memberRepository.findByEmail(oAuth2Attributes.getEmail())
//            .orElseGet(() -> {
//                // 사용자가 없다면 생성
//                Member newMember = Member.builder()
//                        .nickname(oAuth2Attributes.getNickname())
//                        .email(oAuth2Attributes.getEmail())
//                        .profileImage(oAuth2Attributes.getProfileImage())
//                        .role(MemberRole.USER)
//                        .allowance(false)
//                        .build();
//
//                log.info("사용자 가입 이메일: {}", newMember.getEmail());
//                return memberRepository.save(newMember);
//            });
//        log.info("사용자 로그인 이메일: {}", member.getEmail());
//    OAuth2UserDTO oauth2UserDTO = OAuth2UserDTO.from(member);
//}
}
