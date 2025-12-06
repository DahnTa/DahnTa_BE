package com.DahnTa.dto.response;

import lombok.Builder;

@Builder
public record StockOrderResponse(int quantity, double averageUnitPrice, double availableOrderAmount) {

    public static StockOrderResponse create(int quantity, double averageUnitPrice,
        double availableOrderAmount) {

        return StockOrderResponse.builder()
            .quantity(quantity)
            .averageUnitPrice(averageUnitPrice)
            .availableOrderAmount(availableOrderAmount)
            .build();
    }
}
