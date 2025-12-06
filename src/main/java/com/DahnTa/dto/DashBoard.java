package com.DahnTa.dto;

import lombok.Builder;

@Builder
public record DashBoard(Long id, String stockName, String stockTag, double currentPrice, double marketPrice,
                        double changeRate, double changeAmount) {

    public static DashBoard create(Long id, String stockName, String stockTag, double currentPrice,
        double marketPrice, double changeRate, double changeAmount) {

        return DashBoard.builder()
            .id(id)
            .stockName(stockName)
            .stockTag(stockTag)
            .currentPrice(currentPrice)
            .marketPrice(marketPrice)
            .changeRate(changeRate)
            .changeAmount(changeAmount)
            .build();
    }
}
