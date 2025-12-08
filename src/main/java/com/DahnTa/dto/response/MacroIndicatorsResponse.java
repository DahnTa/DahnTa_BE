package com.DahnTa.dto.response;

import java.time.LocalDate;

public record MacroIndicatorsResponse(LocalDate date, String content) {

    public static MacroIndicatorsResponse create(LocalDate date, String content) {

        return new MacroIndicatorsResponse(date, content);
    }
}
