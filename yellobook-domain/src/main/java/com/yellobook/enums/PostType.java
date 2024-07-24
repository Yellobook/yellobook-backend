package com.yellobook.enums;

import lombok.Getter;

@Getter
public enum PostType {
    // 공지
    ANNOUNCE(Values.ANNOUNCE),
    // 주문
    ORDER(Values.ORDER),
    // 업무
    WORK(Values.WORK),;

    private final String value;


    PostType(String value) {
        this.value = value;
    }

    public static PostType fromValue(String value) {
        for(PostType postType : PostType.values()) {
            if(postType.getValue().equals(value)) {
                return postType;
            }
        }
        throw new IllegalArgumentException("%s 는 올바른 enum 값이 아닙니다.".formatted(value));
    }

    // @DiscriminatorValue 에 사용할 수 있도록 private static final 로 우회
    public static class Values {
        public static final String ANNOUNCE = "ANNOUNCE";
        public static final String WORK = "WORK";
        public static final String ORDER = "ORDER";
    }
}
