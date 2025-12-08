package com.DahnTa.dto.request;

import lombok.Getter;

@Getter
public class StockBuyRequest {

    private int quantity;

    protected StockBuyRequest() {}

    public StockBuyRequest(int quantity) {
        this.quantity = quantity;
    }
}
