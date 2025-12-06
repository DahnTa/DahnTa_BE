package com.DahnTa.dto.response;

import java.util.List;

public record TransactionListResponseDTO(
    List<TransactionResponseDTO> transactions
) {}
