package com.DahnTa.entity.Enum;

import lombok.Getter;

@Getter
public enum JwtConstants {
    AUTH_HEADER("Authorization"),
    TOKEN_PREFIX("Bearer "),
    TOKEN_PREFIX_LENGTH("7");

    private final String value;

    JwtConstants(String value) {
        this.value = value;
    }
}
