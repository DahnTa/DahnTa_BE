package com.DahnTa.exception;

import com.DahnTa.dto.response.ErrorResponse;
import com.DahnTa.entity.Enum.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StockException.class)
    public ResponseEntity<ErrorResponse> handleStockException(StockException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.create(errorCode.getMessage(), errorCode.getStatus().value());

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(UserStockException.class)
    public ResponseEntity<ErrorResponse> handleUserStockException(UserStockException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.create(errorCode.getMessage(), errorCode.getStatus().value());

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenException(RefreshTokenException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.create(errorCode.getMessage(), errorCode.getStatus().value());
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.create(errorCode.getMessage(), errorCode.getStatus().value());

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse response = ErrorResponse.create(ErrorCode.INTERNAL_ERROR.getMessage(),
            ErrorCode.INTERNAL_ERROR.getStatus().value());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
