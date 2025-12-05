package com.DahnTa.dto.response;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record StockNewsResponse(LocalDate date, String content) {

    public static StockNewsResponse create(LocalDate date, String content) {

        return StockNewsResponse.builder()
            .date(date)
            .content(content)
            .build();
    }
}
