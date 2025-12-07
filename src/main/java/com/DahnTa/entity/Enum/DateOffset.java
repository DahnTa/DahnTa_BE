package com.DahnTa.entity.Enum;

import lombok.Getter;

@Getter
public enum DateOffset {
    NEXT_DAY(1),
    PREVIOUS_DAY(1);

    private final int days;

    DateOffset(int days) {
        this.days = days;
    }
}
