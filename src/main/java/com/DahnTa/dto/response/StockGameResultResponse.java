package com.DahnTa.dto.response;

public record StockGameResultResponse(double finalReturnRate, double initialFunds, double finalAmount) {

    public static StockGameResultResponse create(double finalReturnRate, double initialFunds,
        double finalAmount) {

        return new StockGameResultResponse(finalReturnRate, initialFunds, finalAmount);
    }
}
