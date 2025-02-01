package com.yellobook.core.error;

public enum CoreErrorType {
    // Inform
    INFORM_NOT_FOUND(CoreErrorCode.INFORM01, CoreErrorKind.NOT_FOUND, "공지가 존재하지 않습니다.", CoreErrorLevel.WARN),
    INFORM_AUTHOR_NOT_MATCH(CoreErrorCode.INFORM02, CoreErrorKind.FORBIDDEN, "공지의 작성자가 아닙니다.", CoreErrorLevel.WARN),
    INFORM_ACCESS_NOT_ALLOWED(CoreErrorCode.INFORM03, CoreErrorKind.FORBIDDEN, "공지에 접근할 권한이 없습니다.",
            CoreErrorLevel.WARN),

    // Inventory
    INVENTORY_NOT_FOUND(CoreErrorCode.INVENTORY01, CoreErrorKind.NOT_FOUND, "해당 재고 현황은 존재하지 않습니다.",
            CoreErrorLevel.WARN),
    INVENTORY_PRODUCT_NOT_FOUND(CoreErrorCode.INVENTORY02, CoreErrorKind.NOT_FOUND, "해당 제품은 존재하지 않습니다.",
            CoreErrorLevel.WARN),
    INVENTORY_PRODUCT_SKU_ALREADY_EXISTS(CoreErrorCode.INVENTORY03, CoreErrorKind.CONFLICT, "이미 존재하는 품번입니다.",
            CoreErrorLevel.ERROR),
    ORDER_RELATED(CoreErrorCode.INVENTORY04, CoreErrorKind.BAD_REQUEST, "주문과 연결되어 있는 제품이라서 삭제가 불가능합니다.",
            CoreErrorLevel.WARN),

    // Member
    MEMBER_NOT_FOUND(CoreErrorCode.MEMBER01, CoreErrorKind.NOT_FOUND, "해당 사용자는 존재하지 않습니다.", CoreErrorLevel.WARN),
    MEMBER_TEAM_NOT_FOUND(CoreErrorCode.MEMBER02, CoreErrorKind.NOT_FOUND, "사용자가 위치한 팀 정보를 찾을 수 없습니다.",
            CoreErrorLevel.WARN),
    NICKNAME_CHANGE_NOT_ALLOWED(CoreErrorCode.MEMBER03, CoreErrorKind.BAD_REQUEST, "닉네임은 30일에 한 번만 변경할 수 있습니다.",
            CoreErrorLevel.WARN),

    // Order
    ORDER_NOT_FOUND(CoreErrorCode.ORDER01, CoreErrorKind.NOT_FOUND, "해당 주문은 존재하지 않습니다.", CoreErrorLevel.WARN),
    ORDER_ACCESS_DENIED(CoreErrorCode.ORDER02, CoreErrorKind.FORBIDDEN, "접근할 수 없는 주문입니다.", CoreErrorLevel.WARN),
    ORDER_CONFIRMED_CANT_MODIFY(CoreErrorCode.ORDER03, CoreErrorKind.BAD_REQUEST, "관리자가 주문 확인한 주문은 주문 정정 요청 불가능합니다.",
            CoreErrorLevel.WARN),
    ORDER_PENDING_MODIFY_CANT_CONFIRM(CoreErrorCode.ORDER04, CoreErrorKind.BAD_REQUEST,
            "관리자가 주문 정정한 주문은 주문 확정 불가능합니다.", CoreErrorLevel.WARN),
    ORDER_CANT_CANCEL(CoreErrorCode.ORDER05, CoreErrorKind.BAD_REQUEST, "주문 정정 상태가 아닌 주문은 취소가 불가능합니다.",
            CoreErrorLevel.WARN),
    ORDER_CREATION_NOT_ALLOWED(CoreErrorCode.ORDER06, CoreErrorKind.FORBIDDEN, "팀에 관리자가 없으면 주문 등록이 불가능합니다.",
            CoreErrorLevel.ERROR),
    ORDER_AMOUNT_EXCEED(CoreErrorCode.ORDER07, CoreErrorKind.BAD_REQUEST, "주문 수량이 제품 수량을 초과할 수 없습니다.",
            CoreErrorLevel.WARN),

    // Team
    MEMBER_NOT_JOINED_ANY_TEAM(CoreErrorCode.TEAM01, CoreErrorKind.NOT_FOUND, "해당 사용자는 아직 팀에 참여하지 않았습니다.",
            CoreErrorLevel.WARN),
    TEAM_NOT_FOUND(CoreErrorCode.TEAM02, CoreErrorKind.NOT_FOUND, "팀을 찾을 수 없습니다.", CoreErrorLevel.WARN),
    USER_NOT_IN_THAT_TEAM(CoreErrorCode.TEAM03, CoreErrorKind.FORBIDDEN, "해당 팀에 속하지 않은 사용자입니다.", CoreErrorLevel.WARN),
    TEAM_CREATION_FAILED(CoreErrorCode.TEAM04, CoreErrorKind.BAD_REQUEST, "팀 생성에 실패하였습니다.", CoreErrorLevel.ERROR),
    TEAM_MEMBER_NOT_FOUND(CoreErrorCode.TEAM05, CoreErrorKind.NOT_FOUND, "팀에서 해당 사용자를 찾을 수 없습니다.",
            CoreErrorLevel.WARN),
    VIEWER_CANNOT_INVITE(CoreErrorCode.TEAM06, CoreErrorKind.FORBIDDEN, "뷰어는 초대권한이 없습니다.", CoreErrorLevel.WARN),
    ADMIN_EXISTS(CoreErrorCode.TEAM07, CoreErrorKind.CONFLICT, "관리자가 이미 존재합니다.", CoreErrorLevel.ERROR),
    INVITATION_NOT_FOUND(CoreErrorCode.TEAM08, CoreErrorKind.NOT_FOUND, "초대장을 찾을 수 없습니다.", CoreErrorLevel.WARN),
    INVALID_INVITATION(CoreErrorCode.TEAM09, CoreErrorKind.BAD_REQUEST, "유효하지 않은 초대장 입니다.", CoreErrorLevel.WARN),
    MEMBER_ALREADY_EXIST(CoreErrorCode.TEAM10, CoreErrorKind.CONFLICT, "이미 팀에 참여한 멤버입니다.", CoreErrorLevel.WARN),
    EXIST_TEAM_NAME(CoreErrorCode.TEAM11, CoreErrorKind.CONFLICT, "이미 존재하는 팀 이름입니다.", CoreErrorLevel.WARN),
    MENTIONED_MEMBER_NOT_FOUND(CoreErrorCode.TEAM12, CoreErrorKind.NOT_FOUND, "존재하지 않는 사용자에 대한 언급입니다.",
            CoreErrorLevel.WARN),

    // Terms
    TERMS_NOT_FOUND(CoreErrorCode.TERMS01, CoreErrorKind.NOT_FOUND, "해당 약관은 존재하지 않습니다.", CoreErrorLevel.WARN);

    private final CoreErrorCode code;
    private final CoreErrorKind kind;
    private final String message;
    private final CoreErrorLevel level;

    CoreErrorType(CoreErrorCode code, CoreErrorKind kind, String message, CoreErrorLevel level) {
        this.code = code;
        this.kind = kind;
        this.message = message;
        this.level = level;
    }

    public String getCode() {
        return code.getCode();
    }

    public CoreErrorKind getKind() {
        return kind;
    }

    public String getMessage() {
        return message;
    }

    public CoreErrorLevel getErrorLevel() {
        return level;
    }
}
