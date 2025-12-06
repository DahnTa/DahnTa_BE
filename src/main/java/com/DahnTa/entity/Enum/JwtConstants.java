package com.DahnTa.entity.Enum;

import lombok.Getter;

@Getter
public enum JwtConstants {
    AUTH_HEADER("Authorization"),
    TOKEN_PREFIX("Bearer "),
    TOKEN_PREFIX_LENGTH(7);

    private final String headerName;
    private final String prefix;
    private final Integer length;

    JwtConstants(String headerName) {
        this.headerName = headerName;
        this.prefix = null;
        this.length = null;
    }

    JwtConstants(String prefix, boolean isPrefix) {
        this.headerName = null;
        this.prefix = prefix;
        this.length = null;
    }

    JwtConstants(int length) {
        this.headerName = null;
        this.prefix = null;
        this.length = length;
    }
}
