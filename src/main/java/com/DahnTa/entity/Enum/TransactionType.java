package com.DahnTa.entity.Enum;

import lombok.Getter;

@Getter
public enum TransactionType {

    BUY("매수"),
    SELL("매도");

    private final String action;

    TransactionType(String action) {
        this.action = action;
    }
}

