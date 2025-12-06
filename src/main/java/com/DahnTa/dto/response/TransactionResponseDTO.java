package com.DahnTa.dto.response;

import com.DahnTa.entity.Transaction;
import com.DahnTa.entity.TransactionType;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class TransactionResponseDTO {
    private LocalDate date;
    private String stockName;
    private String stockTag;
    private TransactionType type;
    private int quantity;
    private double totalAmount;

    public TransactionResponseDTO(LocalDate date, String stockName, String stockTag,
        TransactionType type, int quantity, double totalAmount) {
        this.date = date;
        this.stockName = stockName;
        this.stockTag = stockTag;
        this.type = type;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }
    // Entity -> DTO 변환 메서드
    public static TransactionResponseDTO from(Transaction transaction) {
        return new TransactionResponseDTO(
            transaction.getDate(),
            transaction.getStock().getStockName(),
            transaction.getStock().getStockTag(),
            transaction.getType(),
            transaction.getQuantity(),
            transaction.getTotalAmount()
        );
    }
}
