package com.DahnTa.Mapper;

import com.DahnTa.dto.response.TransactionResponseDTO;
import com.DahnTa.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponseDTO toDTO(Transaction transaction) {
        return TransactionResponseDTO.builder()
            .date(transaction.getDate())
            .stockName(transaction.getStock().getStockName())
            .stockTag(transaction.getStock().getStockTag())
            .type(transaction.getType())
            .quantity(transaction.getQuantity())
            .totalAmount(transaction.getTotalAmount()
            ).build();

    }

}
