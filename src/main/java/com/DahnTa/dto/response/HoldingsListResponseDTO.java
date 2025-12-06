package com.DahnTa.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HoldingsListResponseDTO {
    private List<HoldingsResponseDTO> holdings;
}