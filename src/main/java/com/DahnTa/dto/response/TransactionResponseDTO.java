package com.DahnTa.dto.response;

import com.DahnTa.entity.Transaction;
import com.DahnTa.entity.TransactionType;
import java.time.LocalDate;

public record TransactionResponseDTO(
    LocalDate date,
    String stockName,
    String stockTag,
    TransactionType type,
    int quantity,
    double totalAmount
) {


    public static TransactionResponseDTO from(Transaction tx) {
        return new TransactionResponseDTO(
            tx.getDate(),
            tx.getStock().getStockName(),
            tx.getStock().getStockTag(),
            tx.getType(),
            tx.getQuantity(),
            tx.getTotalAmount()
        );
    }
}
