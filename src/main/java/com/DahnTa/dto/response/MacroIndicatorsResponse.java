package com.DahnTa.dto.response;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record MacroIndicatorsResponse(LocalDate date, String content) {

    public static MacroIndicatorsResponse create(LocalDate date, String content) {

        return MacroIndicatorsResponse.builder()
            .date(date)
            .content(content)
            .build();
    }
}
