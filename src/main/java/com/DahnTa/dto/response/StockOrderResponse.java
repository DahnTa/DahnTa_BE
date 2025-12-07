package com.DahnTa.dto.response;

import lombok.Builder;

@Builder
public record StockOrderResponse(int quantity, double averageUnitPrice, double availableOrderAmount) {

    public static StockOrderResponse create(int quantity, double averageUnitPrice,
        double availableOrderAmount) {

        return new StockOrderResponse(quantity, averageUnitPrice, availableOrderAmount);
    }
}
