package com.yellobook.core.error;

public enum CoreErrorType {
    // 서버 및 시스템 관련 에러
    INTERNAL_SERVER_ERROR(CoreErrorCode.SYS001, "서버 내부 오류가 발생했습니다. 다시 시도해주세요."),
    SERVICE_UNAVAILABLE(CoreErrorCode.SYS002, "현재 서비스 이용이 불가능합니다. 나중에 다시 시도해주세요."),
    GATEWAY_TIMEOUT(CoreErrorCode.SYS003, "요청 시간이 초과되었습니다. 다시 시도해주세요."),

    // 클라이언트 관련 에러
    BAD_REQUEST(CoreErrorCode.CLIENT001, "잘못된 요청입니다. 요청 내용을 다시 확인해주세요."),
    UNAUTHORIZED(CoreErrorCode.CLIENT002, "인증이 필요합니다. 인증 정보를 제공해주세요."),
    FORBIDDEN(CoreErrorCode.CLIENT003, "접근이 거부되었습니다. 권한을 확인해주세요."),
    NOT_FOUND(CoreErrorCode.CLIENT004, "요청한 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(CoreErrorCode.CLIENT005, "허용되지 않은 메서드입니다. 요청 방식을 확인해주세요."),
    NOT_ACCEPTABLE(CoreErrorCode.CLIENT006, "요청한 리소스가 허용되지 않는 형식입니다."),
    UNSUPPORTED_MEDIA_TYPE(CoreErrorCode.CLIENT007, "지원되지 않는 미디어 형식입니다."),
    TOO_MANY_REQUESTS(CoreErrorCode.CLIENT008, "요청이 너무 많습니다. 잠시 후 다시 시도해주세요."),

    /*
    파일
     */
    FILE_NOT_EXIST(CoreErrorCode.FILE001, "파일이 존재하지 않습니다."),
    FILE_NOT_EXCEL(CoreErrorCode.FILE002, "올바른 엑셀 확장자가 아닙니다. .xlsx의 엑셀 파일이어야 합니다."),
    CELL_IS_EMPTY(CoreErrorCode.FILE003, "빈 값을 가진 셀이 존재합니다."),
    CELL_INVALID_TYPE(CoreErrorCode.FILE004, "셀의 타입이 올바르지 않습니다."),
    ROW_HAS_EMPTY_CELL(CoreErrorCode.FILE005, "row에 빈 cell이 존재합니다."),
    FILE_IO_FAIL(CoreErrorCode.FILE006, "파일 IO에 실패했습니다."),
    SKU_DUPLICATE(CoreErrorCode.FILE007, "중복된 SKU가 존재합니다."),
    INT_OVER_ONE(CoreErrorCode.FILE008, "SKU, 구매가, 판매가, 현재 재고 수량은 0 이상이여야 합니다."),


    /*
    인증
     */
    // 인증 및 권한 관련 에러
    AUTHENTICATION_FAILED(CoreErrorCode.AUTH001, "사용자 인증에 실패했습니다."),
    ACCESS_DENIED(CoreErrorCode.AUTH002, "접근이 거부되었습니다. 이 리소스에 대한 권한이 없습니다."),
    INSUFFICIENT_PERMISSIONS(CoreErrorCode.AUTH003, "작업을 수행할 권한이 부족합니다."),
    LOGIN_FAILED(CoreErrorCode.AUTH004, "로그인에 실패했습니다."),

    // 토큰 관련 에러
    ACCESS_TOKEN_EXPIRED(CoreErrorCode.AUTH005, "엑세스 토큰의 유효기간이 만료되었습니다."),
    REFRESH_TOKEN_EXPIRED(CoreErrorCode.AUTH005, "리프레시 토큰의 유효기간이 만료되었습니다."),
    TERMS_TOKEN_EXPIRED(CoreErrorCode.AUTH006, "약관 동의 토큰의 유효기간이 만료되었습니다."),
    INVALID_TOKEN_FORMAT(CoreErrorCode.AUTH007, "잘못된 토큰 형식입니다."),
    INVALID_TOKEN_SIGNATURE(CoreErrorCode.AUTH008, "토큰의 서명이 일치하지 않습니다."),
    UNSUPPORTED_TOKEN(CoreErrorCode.AUTH009, "토큰의 특정 헤더나 클레임이 지원되지 않습니다."),

    REFRESH_TOKEN_NOT_FOUND(CoreErrorCode.AUTH010, "쿠키에 리프레시 토큰이 없습니다."),
    ACCESS_TOKEN_NOT_FOUND(CoreErrorCode.AUTH011, "요청 헤더에 엑세스 토큰이 없습니다."),
    // 권한 애러
    ADMIN_NOT_ALLOWED(CoreErrorCode.AUTH012, "관리자는 접근 권한이 없습니다."),
    ORDERER_NOT_ALLOWED(CoreErrorCode.AUTH013, "주문자는 접근 권한이 없습니다."),
    VIEWER_NOT_ALLOWED(CoreErrorCode.AUTH014, "뷰어는 접근 권한이 없습니다."),
    USER_NOT_EXIST(CoreErrorCode.AUTH015, "가입한 사용자가 존재하지 않습니다."),

    /*
    공지
     */
    INFORM_NOT_FOUND(CoreErrorCode.INFORM001, "공지(업무)가 존재하지 않습니다."),
    INFORM_MEMBER_NOT_MATCH(CoreErrorCode.INFORM002, "공지(업무)의 작성자가 아닙니다."),
    NOT_MENTIONED(CoreErrorCode.INFORM003, "공지(업무)에 언급되지 않은 멤버입니다."),

    /*
    재고
     */
    INVENTORY_NOT_FOUND(CoreErrorCode.INVENTORY001, "해당 재고 현황은 존재하지 않습니다."),
    PRODUCT_NOT_FOUND(CoreErrorCode.INVENTORY002, "해당 제품은 존재하지 않습니다."),
    SKU_ALREADY_EXISTS(CoreErrorCode.INVENTORY003, "이미 존재하는 품번입니다."),
    ORDER_RELATED(CoreErrorCode.INVENTORY004, "주문과 연결되어 있는 제품이라서 삭제가 불가능합니다."),
    /*
    사용자
     */
    MEMBER_NOT_FOUND(CoreErrorCode.MEMBER001, "해당 사용자는 존재하지 않습니다."),
    MEMBER_TEAM_NOT_FOUND(CoreErrorCode.MEMBER002, "사용자가 위치한 팀 정보를 찾을 수 없습니다."),
    /*
    주문
     */
    ORDER_NOT_FOUND(CoreErrorCode.ORDER001, "해당 주문은 존재하지 않습니다."),
    ORDER_ACCESS_DENIED(CoreErrorCode.ORDER002, "접근할 수 없는 주문입니다."),
    ORDER_CONFIRMED_CANT_MODIFY(CoreErrorCode.ORDER003, "관리자가 주문 확인한 주문은 주문 정정 요청 불가능합니다."),
    ORDER_PENDING_MODIFY_CANT_CONFIRM(CoreErrorCode.ORDER004, "관리자가 주문 정정한 주문은 주문 확정 불가능합니다."),
    ORDER_CANT_CANCEL(CoreErrorCode.ORDER005, "주문 정정 상태가 아닌 주문은 최소가 불가능 합니다."),
    ORDER_CREATION_NOT_ALLOWED(CoreErrorCode.ORDER006, "팀에 관리자가 없으면 주문 등록이 불가능 합니다."),
    ORDER_AMOUNT_EXCEED(CoreErrorCode.ORDER007, "주문 수량이 제품 수량을 초과할 수 없습니다."),
    ONLY_ORDERER_CAN_ORDER(CoreErrorCode.ORDER008, "오직 주문자만 주문할 수 있습니다."),

    /*
    팀
     */
    USER_NOT_JOINED_ANY_TEAM(CoreErrorCode.TEAM001, "해당 사용자는 아직 팀에 참여하지 않았습니다."),
    TEAM_NOT_FOUND(CoreErrorCode.TEAM002, "팀을 찾을 수 없습니다."),
    USER_NOT_IN_THAT_TEAM(CoreErrorCode.TEAM003, "해당 팀에 속하지 않은 사용자입니다."),
    TEAM_CREATION_FAILED(CoreErrorCode.TEAM004, "팀 생성에 실패하였습니다."),
    TEAM_MEMBER_NOT_FOUND(CoreErrorCode.TEAM005, "팀에서 해당 사용자를 찾을 수 없습니다."),
    VIEWER_CANNOT_INVITE(CoreErrorCode.TEAM006, "뷰어는 초대권한이 없습니다."),
    ADMIN_EXISTS(CoreErrorCode.TEAM007, "관리자가 이미 존재합니다."),
    INVITATION_NOT_FOUND(CoreErrorCode.TEAM008, "초대장을 찾을 수 없습니다."),
    INVALID_INVITATION(CoreErrorCode.TEAM009, "유효하지 않은 초대장 입니다."),
    MEMBER_ALREADY_EXIST(CoreErrorCode.TEAM010, "이미 존재하는 멤버입니다."),
    EXIST_TEAM_NAME(CoreErrorCode.TEAM011, "이미 존재하는 팀 이름입니다."),
    MENTIONED_MEMBER_NOT_FOUND(CoreErrorCode.TEAM012, "존재하지 않는 사용자에 대한 언급입니다.");

    private final CoreErrorCode code;
    private final String message;

    CoreErrorType(CoreErrorCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code.getCode();
    }

    public String getMessage() {
        return message;
    }
}