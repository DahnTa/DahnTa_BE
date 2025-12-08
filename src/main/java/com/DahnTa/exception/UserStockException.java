package com.DahnTa.exception;

import com.DahnTa.entity.Enum.ErrorCode;
import lombok.Getter;

@Getter
public class UserStockException extends RuntimeException {

    private final ErrorCode errorCode;

    public UserStockException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
