package com.DahnTa.dto.response;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record StockTotalAnalysisResponse(LocalDate date, String companyName, String analyze) {

    public static StockTotalAnalysisResponse create(LocalDate date, String companyName, String analyze) {

        return StockTotalAnalysisResponse.builder()
            .date(date)
            .companyName(companyName)
            .analyze(analyze)
            .build();
    }
}
