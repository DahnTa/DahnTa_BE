package com.DahnTa.dto.request;

import lombok.Getter;

@Getter
public class StockSellRequest {

    private int quantity;

    protected StockSellRequest() {}

    public StockSellRequest(int quantity) {
        this.quantity = quantity;
    }
}
