package com.DahnTa.dto.response;

import lombok.Builder;

@Builder
public record StockOrderResponse(int quantity, int averageUnitPrice, int availableOrderAmount) {

    public static StockOrderResponse create(int quantity, int averageUnitPrice, int availableOrderAmount) {

        return StockOrderResponse.builder()
            .quantity(quantity)
            .averageUnitPrice(averageUnitPrice)
            .availableOrderAmount(availableOrderAmount)
            .build();
    }
}
