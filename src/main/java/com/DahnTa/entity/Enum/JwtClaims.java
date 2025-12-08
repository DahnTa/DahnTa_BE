package com.DahnTa.entity.Enum;

import lombok.Getter;

@Getter
public enum JwtClaims {
    USER_ID("userId"),
    USER_ACCOUNT("userAccount");

    private final String value;

    JwtClaims(String value) {
        this.value = value;
    }
}
