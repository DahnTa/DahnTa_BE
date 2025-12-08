package com.DahnTa.dto;

import lombok.Builder;

public record DashBoard(Long id, String stockName, String stockTag, double currentPrice, double marketPrice,
                        double changeRate, double changeAmount) {

    public static DashBoard create(Long id, String stockName, String stockTag, double currentPrice,
        double marketPrice, double changeRate, double changeAmount) {

        return new DashBoard(id, stockName, stockTag, currentPrice, marketPrice, changeRate, changeAmount);
    }
}
