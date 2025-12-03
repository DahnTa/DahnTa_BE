package com.DahnTa.dto;

import lombok.Builder;

@Builder
public record DashBoard(Long id, String stockName, String stockTag, int currentPrice, int marketPrice,
                        double changeRate, int changeAmount) {

    public static DashBoard create(Long id, String stockName, String stockTag, int currentPrice,
        int marketPrice, double changeRate, int changeAmount) {

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
