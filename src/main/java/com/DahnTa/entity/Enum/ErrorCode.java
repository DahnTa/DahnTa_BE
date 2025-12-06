package com.DahnTa.entity.Enum;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    STOCK_NOT_FOUND("주식을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PRICE_NOT_FOUND("해당 날짜의 주식 가격을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    POSSESSION_NOT_FOUND("해당 주식을 보유하고 있지 않습니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_CREDIT("보유 금액이 부족합니다.", HttpStatus.BAD_REQUEST),
    INVALID_QUANTITY("수량이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    GAME_DATE_NOT_FOUND("해당 user의 GameDate를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    COMPANY_FINANCE_NOT_FOUND("해당 날짜의 주식 재무제표를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MACRO_INDICATORS_NOT_FOUND("해당 날짜의 거시경제 지표를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NEWS_NOT_FOUND("해당 날짜의 주식 뉴스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    REDDIT_NOT_FOUND("해당 날짜의 주식 Reddit 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    TOTAL_ANALYSIS_NOT_FOUND("해당 날짜의 주식 종합분석을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INSUFFICIENT_HOLDINGS("보유 수량보다 많이 팔 수 없습니다.", HttpStatus.BAD_REQUEST),

    INTEREST_NOT_FOUND("관심 주식이 없습니다.", HttpStatus.NOT_FOUND),
    ALREADY_INTERESTED("이미 관심 주식으로 등록되어 있습니다.", HttpStatus.CONFLICT),
    PRICE_INFO_NOT_FOUND("가격 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    REFRESH_TOKEN_NOT_FOUND("해당 사용자의 리프레시 토큰이 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ACCOUNT_ALREADY_EXISTS("이미 존재하는 계정입니다.", HttpStatus.CONFLICT),
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),

    INTERNAL_ERROR("서버 내부 오류", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
