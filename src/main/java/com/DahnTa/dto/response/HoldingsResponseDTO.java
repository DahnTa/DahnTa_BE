package com.DahnTa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HoldingsResponseDTO {
    private String stockName;
    private String stockTag;
    private double quantity;
    private double changeRate;          // 어제 대비 변동률
    private double stockValuation;      // 평가 금액
}


