//package com.yellobook.core.domain.inform;
//
//import com.yellobook.core.domain.member.Member;
//import com.yellobook.core.domain.member.MemberReader;
//import jakarta.transaction.Transactional;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//public class InformService {
//    private final InformReader informReader;
//    private final InformWriter informWriter;
//    private final InformViewProcessor informViewProcessor;
//    private final MemberReader memberReader;
//    private final InformAccessManager informAccessManager;
//
//    public InformService(InformReader informReader, InformWriter informWriter, InformViewProcessor informViewProcessor,
//                         MemberReader memberReader, InformAccessManager informAccessManager) {
//        this.informReader = informReader;
//        this.informWriter = informWriter;
//        this.informViewProcessor = informViewProcessor;
//        this.memberReader = memberReader;
//        this.informAccessManager = informAccessManager;
//    }
//
//    @Transactional
//    public Long create(String title, String content, Member author, List<Long> mentionedMemberIds)
//    ) {
//        List<Member>
//        return informWriter.create(createInformCommend);
//    }
//
//    // Infom 도메인에 필요한 객체는
//    // Author, MentionedMember
//    // 조회로직에는 Author 인지, MentioendMember 인지 모름 ...
//    // 그래서, AuthorOrMentionedMember
//    // 변환이 ..... 엄청마
//    // Member 객체를 아예 이용하는게 좋을까?
//
//    // 그러면, Author 는 왜 ?? 필요한가?
//    // 어차피 Member 로 퉁칠꺼면 Author 는 조회전용이 되는거 아닌가?
//
//    // 작성자 "만"
//    @Transactional
//    public void delete(Long informId, Member member) {
//        informAccessManager.validateAuthor(informId, member);
//        informWriter.delete(informId);
//    }
//
//    // 작성자 + 언급된 사용자
//    @Transactional
//    public void increaseView(Long informId, Member member) {
//        informAccessManager.validateAccess(informId, memberId);
//        informViewProcessor.increase(informId);
//    }
//
//    public Inform read(Long informId, Member member) {
//        informAccessManager.validateAccess(informId, memberId);
//        return informReader.read(informId);
//    }
//}