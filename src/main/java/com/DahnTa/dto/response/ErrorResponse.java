package com.DahnTa.dto.response;

import lombok.Builder;

@Builder
public record ErrorResponse(String message, int status) {

    public static ErrorResponse create(String message, int status) {

        return ErrorResponse.builder()
            .message(message)
            .status(status)
            .build();
    }
}
