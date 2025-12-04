package com.DahnTa.dto.response;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record MacroIndicatorsResponse(LocalDate date, String disclaimer, String content) {

    public static MacroIndicatorsResponse create(LocalDate date, String disclaimer, String content) {

        return MacroIndicatorsResponse.builder()
            .date(date)
            .disclaimer(disclaimer)
            .content(content)
            .build();
    }
}
