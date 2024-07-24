package com.yellobook.enums;

import lombok.Getter;

@Getter
public enum CommentType {
    // 공지 댓글
    ANNOUNCE(Values.ANNOUNCE_COMMENT),
    // 주문 댓글
    ORDER(Values.ORDER_COMMENT),
    // 업무 댓글
    WORK(Values.WORK_COMMENT),;

    private final String value;


    CommentType(String value) {
        this.value = value;
    }

    public static CommentType fromValue(String value) {
        for(CommentType commentType : CommentType.values()) {
            if(commentType.getValue().equals(value)) {
                return commentType;
            }
        }
        throw new IllegalArgumentException("%s 는 올바른 enum 값이 아닙니다.".formatted(value));
    }

    // @DiscriminatorValue 에 사용할 수 있도록 private static final 로 우회
    public static class Values {
        public static final String ANNOUNCE_COMMENT = "ANNOUNCE_COMMENT";
        public static final String WORK_COMMENT = "WORK_COMMENT";
        public static final String ORDER_COMMENT = "ORDER_COMMENT";
    }
}

