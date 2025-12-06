package com.DahnTa.dto.response;

import java.util.List;

public record HoldingsListResponseDTO(
    List<HoldingsResponseDTO> holdings
) {}
