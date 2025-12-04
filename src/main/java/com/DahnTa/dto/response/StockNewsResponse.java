package com.DahnTa.dto.response;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record StockNewsResponse(LocalDate date, String disclaimer, String content) {

    public static StockNewsResponse create(LocalDate date, String disclaimer, String content) {

        return StockNewsResponse.builder()
            .date(date)
            .disclaimer(disclaimer)
            .content(content)
            .build();
    }
}
