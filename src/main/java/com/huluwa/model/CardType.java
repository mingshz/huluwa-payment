package com.huluwa.model;

public enum CardType {
    CREDIT_CARD("信用卡"),
    CASH_CARD("储蓄卡");

    private String type;

    private CardType(String type){
        this.type = type;
    }
}
