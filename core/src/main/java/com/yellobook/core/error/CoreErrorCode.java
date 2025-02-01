package com.yellobook.core.error;

public enum CoreErrorCode {
    SYS01("SYS-01"),
    SYS02("SYS-02"),
    SYS03("SYS-03"),
    CLIENT01("CLIENT-01"),
    CLIENT02("CLIENT-02"),
    CLIENT03("CLIENT-03"),
    CLIENT04("CLIENT-04"),
    CLIENT05("CLIENT-05"),
    CLIENT06("CLIENT-06"),
    CLIENT07("CLIENT-07"),
    CLIENT08("CLIENT-08"),
    FILE01("FILE-01"),
    FILE02("FILE-02"),
    FILE03("FILE-03"),
    FILE04("FILE-04"),
    FILE05("FILE-05"),
    FILE06("FILE-06"),
    FILE07("FILE-07"),
    FILE08("FILE-08"),
    AUTH01("AUTH-01"),
    AUTH02("AUTH-02"),
    AUTH03("AUTH-03"),
    AUTH04("AUTH-04"),
    AUTH05("AUTH-05"),
    AUTH06("AUTH-06"),
    AUTH07("AUTH-07"),
    AUTH08("AUTH-08"),
    AUTH09("AUTH-09"),
    AUTH10("AUTH-00"),
    AUTH11("AUTH-01"),
    AUTH12("AUTH-02"),
    AUTH13("AUTH-03"),
    AUTH14("AUTH-04"),
    AUTH15("AUTH05"),
    INFORM01("INFORM-01"),
    INFORM02("INFORM-02"),
    INFORM03("INFORM-03"),
    INVENTORY01("INVENTORY-01"),
    INVENTORY02("INVENTORY-02"),
    INVENTORY03("INVENTORY-03"),
    INVENTORY04("INVENTORY-04"),
    MEMBER01("MEMBER-01"),
    MEMBER02("MEMBER-02"),
    MEMBER03("MEMBER-03"),
    ORDER01("ORDER-01"),
    ORDER02("ORDER-02"),
    ORDER03("ORDER-03"),
    ORDER04("ORDER-04"),
    ORDER05("ORDER-05"),
    ORDER06("ORDER-06"),
    ORDER07("ORDER-07"),
    ORDER08("ORDER-08"),
    TEAM01("TEAM-01"),
    TEAM02("TEAM-02"),
    TEAM03("TEAM-03"),
    TEAM04("TEAM-04"),
    TEAM05("TEAM-05"),
    TEAM06("TEAM-06"),
    TEAM07("TEAM-07"),
    TEAM08("TEAM-08"),
    TEAM09("TEAM-09"),
    TEAM10("TEAM-00"),
    TEAM11("TEAM-01"),
    TEAM12("TEAM-02"),
    TERMS01("TERMS-01");

    private final String code;

    CoreErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
