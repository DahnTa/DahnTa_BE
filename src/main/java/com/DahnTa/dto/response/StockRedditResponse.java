package com.DahnTa.dto.response;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record StockRedditResponse(LocalDate date, String content, int score, int numComment) {

    public static StockRedditResponse create(LocalDate date, String content, int score, int numComment) {

        return StockRedditResponse.builder()
            .date(date)
            .content(content)
            .score(score)
            .numComment(numComment)
            .build();
    }
}
