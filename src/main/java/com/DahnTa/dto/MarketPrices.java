package com.DahnTa.dto;

import lombok.Builder;

@Builder
public record MarketPrices(double marketPrice) {

    public static MarketPrices create(double marketPrice) {

        return MarketPrices.builder()
            .marketPrice(marketPrice)
            .build();
    }
}
