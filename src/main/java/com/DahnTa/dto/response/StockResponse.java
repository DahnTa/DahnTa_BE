package com.DahnTa.dto.response;

import com.DahnTa.dto.MarketPrices;
import java.util.List;
import lombok.Builder;

@Builder
public record StockResponse(String stockName, String stockTag, List<MarketPrices> marketPrices,
                            double currentPrice, double changeRate) {

    public static StockResponse create(String stockName, String stockTag, List<MarketPrices> marketPrices,
        double currentPrice, double changeRate) {

        return new StockResponse(stockName, stockTag, marketPrices, currentPrice, changeRate);
    }
}
