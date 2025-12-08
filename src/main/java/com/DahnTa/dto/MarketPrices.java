package com.DahnTa.dto;

import lombok.Builder;

public record MarketPrices(double marketPrice) {

    public static MarketPrices create(double marketPrice) {

        return new MarketPrices(marketPrice);
    }
}
