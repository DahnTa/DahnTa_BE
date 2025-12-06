package com.DahnTa.dto.response;

import com.DahnTa.entity.Enum.TransactionType;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record TransactionResponseDTO(
    LocalDate date,
    String stockName,
    String stockTag,
    TransactionType type,
    int quantity,
    double totalAmount
) {

}
