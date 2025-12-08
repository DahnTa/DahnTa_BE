package com.DahnTa.exception;

import com.DahnTa.entity.Enum.ErrorCode;
import lombok.Getter;

@Getter
public class StockException extends RuntimeException {

    private final ErrorCode errorCode;

    public StockException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
