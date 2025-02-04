package com.yellobook.core.error;

public enum CoreErrorType {
    // Inform
    INFORM_NOT_FOUND(CoreErrorCode.INFORM001, CoreErrorKind.NOT_FOUND, "공지가 존재하지 않습니다.", CoreErrorLevel.WARN),
    INFORM_AUTHOR_NOT_MATCH(CoreErrorCode.INFORM002, CoreErrorKind.FORBIDDEN, "공지의 작성자가 아닙니다.", CoreErrorLevel.WARN),
    INFORM_ACCESS_NOT_ALLOWED(CoreErrorCode.INFORM003, CoreErrorKind.FORBIDDEN, "공지에 접근할 권한이 없습니다.",
            CoreErrorLevel.WARN),

    // Inventory
    INVENTORY_NOT_FOUND(CoreErrorCode.INVENTORY001, CoreErrorKind.NOT_FOUND, "해당 재고 현황은 존재하지 않습니다.",
            CoreErrorLevel.WARN),
    PRODUCT_NOT_FOUND(CoreErrorCode.INVENTORY002, CoreErrorKind.NOT_FOUND, "해당 제품은 존재하지 않습니다.",
            CoreErrorLevel.WARN),
    PRODUCT_SKU_ALREADY_EXISTS(CoreErrorCode.INVENTORY003, CoreErrorKind.CONFLICT, "이미 존재하는 품번입니다.",
            CoreErrorLevel.ERROR), // 시스템 데이터 중복은 ERROR
    ORDER_RELATED(CoreErrorCode.INVENTORY004, CoreErrorKind.BAD_REQUEST, "주문과 연결되어 있는 제품이라서 삭제가 불가능합니다.",
            CoreErrorLevel.WARN),
    VIEWER_CANT_ACCESS_INVENTORY(CoreErrorCode.INVENTORY005, CoreErrorKind.CONFLICT, "뷰어는 재고 현황에 접근할 수 없습니다",
            CoreErrorLevel.WARN),
    TEAM_INVENTORY_EMPTY(CoreErrorCode.INVENTORY006, CoreErrorKind.NOT_FOUND, "팀에 재고 현황이 존재하지 않습니다",
            CoreErrorLevel.WARN),
    ONLY_ADMIN_CAN_MANIPULATE_INVENTORY(CoreErrorCode.INVENTORY007, CoreErrorKind.FORBIDDEN, "오직 관리자만 재고를 관리할 수 있습니다.",
            CoreErrorLevel.WARN),

    // Member
    MEMBER_NOT_FOUND(CoreErrorCode.MEMBER001, CoreErrorKind.NOT_FOUND, "해당 사용자는 존재하지 않습니다.", CoreErrorLevel.WARN),
    MEMBER_TEAM_NOT_FOUND(CoreErrorCode.MEMBER002, CoreErrorKind.NOT_FOUND, "사용자가 위치한 팀 정보를 찾을 수 없습니다.",
            CoreErrorLevel.WARN),

    // Order
    ORDER_NOT_FOUND(CoreErrorCode.ORDER001, CoreErrorKind.NOT_FOUND, "해당 주문은 존재하지 않습니다.", CoreErrorLevel.WARN),
    ORDER_ACCESS_DENIED(CoreErrorCode.ORDER002, CoreErrorKind.FORBIDDEN, "접근할 수 없는 주문입니다.", CoreErrorLevel.WARN),
    ORDER_CONFIRMED_CANT_MODIFY(CoreErrorCode.ORDER003, CoreErrorKind.BAD_REQUEST, "관리자가 주문 확인한 주문은 주문 정정 요청 불가능합니다.",
            CoreErrorLevel.WARN),
    ORDER_PENDING_MODIFY_CANT_CONFIRM(CoreErrorCode.ORDER004, CoreErrorKind.BAD_REQUEST,
            "관리자가 주문 정정한 주문은 주문 확정 불가능합니다.", CoreErrorLevel.WARN),
    ORDER_CANT_CANCEL(CoreErrorCode.ORDER005, CoreErrorKind.BAD_REQUEST, "주문 정정 상태가 아닌 주문은 취소가 불가능합니다.",
            CoreErrorLevel.WARN),
    ORDER_CREATION_NOT_ALLOWED(CoreErrorCode.ORDER006, CoreErrorKind.FORBIDDEN, "팀에 관리자가 없으면 주문 등록이 불가능합니다.",
            CoreErrorLevel.ERROR),
    ORDER_AMOUNT_EXCEED(CoreErrorCode.ORDER007, CoreErrorKind.BAD_REQUEST, "주문 수량이 제품 수량을 초과할 수 없습니다.",
            CoreErrorLevel.WARN),

    // Team
    MEMBER_NOT_JOINED_ANY_TEAM(CoreErrorCode.TEAM001, CoreErrorKind.NOT_FOUND, "해당 사용자는 아직 팀에 참여하지 않았습니다.",
            CoreErrorLevel.WARN),
    TEAM_NOT_FOUND(CoreErrorCode.TEAM002, CoreErrorKind.NOT_FOUND, "팀을 찾을 수 없습니다.", CoreErrorLevel.WARN),
    USER_NOT_IN_THAT_TEAM(CoreErrorCode.TEAM003, CoreErrorKind.FORBIDDEN, "해당 팀에 속하지 않은 사용자입니다.", CoreErrorLevel.WARN),
    TEAM_CREATION_FAILED(CoreErrorCode.TEAM004, CoreErrorKind.BAD_REQUEST, "팀 생성에 실패하였습니다.", CoreErrorLevel.ERROR),
    TEAM_MEMBER_NOT_FOUND(CoreErrorCode.TEAM005, CoreErrorKind.NOT_FOUND, "팀에서 해당 사용자를 찾을 수 없습니다.",
            CoreErrorLevel.WARN),
    ONLY_ADMIN_CAN_MAKE_CODE(CoreErrorCode.TEAM006, CoreErrorKind.FORBIDDEN, "오직 관리자만 초대 코드를 생성할 수 있습니다.",
            CoreErrorLevel.WARN),
    ADMIN_EXISTS(CoreErrorCode.TEAM007, CoreErrorKind.CONFLICT, "관리자가 이미 존재합니다.", CoreErrorLevel.ERROR),
    INVITATION_NOT_FOUND(CoreErrorCode.TEAM008, CoreErrorKind.NOT_FOUND, "초대장을 찾을 수 없습니다.", CoreErrorLevel.WARN),
    INVALID_INVITATION(CoreErrorCode.TEAM009, CoreErrorKind.BAD_REQUEST, "유효하지 않은 초대장 입니다.", CoreErrorLevel.WARN),
    MEMBER_ALREADY_EXIST(CoreErrorCode.TEAM010, CoreErrorKind.CONFLICT, "이미 팀에 참여한 멤버입니다.", CoreErrorLevel.WARN),
    EXIST_TEAM_NAME(CoreErrorCode.TEAM011, CoreErrorKind.CONFLICT, "이미 존재하는 팀 이름입니다.", CoreErrorLevel.WARN),
    MENTIONED_MEMBER_NOT_FOUND(CoreErrorCode.TEAM012, CoreErrorKind.NOT_FOUND, "존재하지 않는 사용자에 대한 언급입니다.",
            CoreErrorLevel.WARN),
    ONLY_ADMIN_CAN_UPDATE(CoreErrorCode.TEAM013, CoreErrorKind.CONFLICT, "오직 관리자만 변경할 수 있는 정보 입니다.",
            CoreErrorLevel.WARN),
    DID_NOT_APPLY(CoreErrorCode.TEAM014, CoreErrorKind.CONFLICT, "가입 요청을 한적 없습니다.", CoreErrorLevel.WARN),
    ADMIN_AND_ORDERER_CAN_UPDATE_JOIN_REQUEST(CoreErrorCode.TEAM015, CoreErrorKind.CONFLICT,
            "오직 관리자와 주문자만 가입 요청을 승인 또는 거절할 수 있습니다.", CoreErrorLevel.WARN),
    APPLY_TEAM_NOT_FOUND(CoreErrorCode.TEAM016, CoreErrorKind.NOT_FOUND, "존재하지 않는 가입 요청입니다.", CoreErrorLevel.WARN),
    ONLY_VIEWER_CAN_REQUESTED_ORDERER_CONVERSION(CoreErrorCode.TEAM017, CoreErrorKind.CONFLICT,
            "뷰어만 주문자로 권한 변경을 요청할 수 있습니다.", CoreErrorLevel.WARN),
    ALREADY_REQUESTED_ORDERER_CONVERSION(CoreErrorCode.TEAM018, CoreErrorKind.CONFLICT, "이미 주문자로 권한 변경 요청을 했습니다.",
            CoreErrorLevel.WARN),
    ROLE_CONVERSION_NOT_FOUND(CoreErrorCode.TEAM019, CoreErrorKind.NOT_FOUND, "권한 변경 요청이 존재하지 않습니다.",
            CoreErrorLevel.WARN),
    ;


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
