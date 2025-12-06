package com.DahnTa.dto.response;

import lombok.Builder;

@Builder
public record StockGameResultResponse(double finalReturnRate, double initialFunds, double finalAmount) {

    public static StockGameResultResponse create(double finalReturnRate, double initialFunds,
        double finalAmount) {

        return StockGameResultResponse.builder()
            .finalReturnRate(finalReturnRate)
            .initialFunds(initialFunds)
            .finalAmount(finalAmount)
            .build();

    }
}
