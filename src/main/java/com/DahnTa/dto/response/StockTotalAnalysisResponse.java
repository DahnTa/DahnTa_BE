package com.DahnTa.dto.response;

import java.time.LocalDate;

public record StockTotalAnalysisResponse(LocalDate date, String companyName, String analyze) {

    public static StockTotalAnalysisResponse create(LocalDate date, String companyName, String analyze) {

        return new StockTotalAnalysisResponse(date, companyName, analyze);
    }
}
