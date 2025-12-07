package com.DahnTa.dto.response;

import java.time.LocalDate;

public record StockCompanyFinanceResponse(LocalDate date, String content) {

    public static StockCompanyFinanceResponse create(LocalDate date, String content) {

        return new StockCompanyFinanceResponse(date, content);
    }
}
