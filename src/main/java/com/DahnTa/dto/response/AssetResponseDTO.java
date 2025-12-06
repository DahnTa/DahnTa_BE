package com.DahnTa.dto.response;

public record AssetResponseDTO(
    double totalAmount,
    double creditChangeRate,
    double creditChangeAmount,
    double userCredit,
    double stockValuation
) {}
