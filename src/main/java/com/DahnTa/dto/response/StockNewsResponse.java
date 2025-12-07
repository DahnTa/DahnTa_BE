package com.DahnTa.dto.response;

import java.time.LocalDate;

public record StockNewsResponse(LocalDate date, String content) {

    public static StockNewsResponse create(LocalDate date, String content) {

        return new StockNewsResponse(date, content);
    }
}
