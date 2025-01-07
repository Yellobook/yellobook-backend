package com.yellobook.core.domain.inform;

import java.time.LocalDate;
import java.util.List;


// 함께 생성되고 함께 삭제되는 것들
public class Inform {
    private Long informId;
    private String title;
    private String content;
    private int view;
    private LocalDate plannedDate;
    private Author author;
    private List<MentionedMember> mentionedMembers;
}
