package com.DahnTa.dto.response;

public record HoldingsResponseDTO(
    String stockName,
    String stockTag,
    int quantity,
    double changeRate,
    double stockValuation
) {}
