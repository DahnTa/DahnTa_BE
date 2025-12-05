package com.DahnTa.dto.response;

import lombok.Builder;

@Builder
public record StockGameResultResponse(double finalReturnRate, int initialFunds, int finalAmount) {

    public static StockGameResultResponse create(double finalReturnRate, int initialFunds,
        int finalAmount) {

        return StockGameResultResponse.builder()
            .finalReturnRate(finalReturnRate)
            .initialFunds(initialFunds)
            .finalAmount(finalAmount)
            .build();

    }
}
