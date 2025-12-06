package com.DahnTa.dto.response;

import com.DahnTa.entity.Interest;

public class InterestResponseDTO {
    private Long stockId;
    private String stockName;
    private String stockTag;
    private double currentPrice;
    private double changeRate;


    public InterestResponseDTO(Long stockId, String stockName, String stockTag,
        double currentPrice, double changeRate) {
        this.stockId = stockId;
        this.stockName = stockName;
        this.stockTag = stockTag;
        this.currentPrice = currentPrice;
        this.changeRate = changeRate;
    }
}
