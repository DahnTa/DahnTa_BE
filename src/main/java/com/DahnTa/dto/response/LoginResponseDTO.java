package com.DahnTa.dto.response;

public record LoginResponseDTO(
    String accessToken,
    String refreshToken
) {}
