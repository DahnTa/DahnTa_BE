package com.DahnTa.entity.Enum;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public enum GameDateSetting {

    START(LocalDate.of(2024, 10, 1)),
    END(LocalDate.of(2025, 10, 1)),
    DAY_DURATION(19),
    OFFSET_DAY(10),
    DAYS_TO_SUBTRACT(29),
    START_DAY(1);

    private final LocalDate dateValue;
    private final Integer numberValue;

    GameDateSetting(LocalDate dateValue) {
        this.dateValue = dateValue;
        this.numberValue = null;
    }

    GameDateSetting(int numberValue) {
        this.dateValue = null;
        this.numberValue = numberValue;
    }
}
