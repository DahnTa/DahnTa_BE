package com.DahnTa.dto;

import lombok.Builder;

@Builder
public record MarketPrices(int marketPrice) {

    public static MarketPrices create(int marketPrice) {

        return MarketPrices.builder()
            .marketPrice(marketPrice)
            .build();
    }
}
