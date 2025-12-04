package com.DahnTa.dto.response;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record StockCompanyFinanceResponse(LocalDate date, String disclaimer, String content) {

    public static StockCompanyFinanceResponse create(LocalDate date, String disclaimer, String content) {

        return StockCompanyFinanceResponse.builder()
            .date(date)
            .disclaimer(disclaimer)
            .content(content)
            .build();
    }
}
