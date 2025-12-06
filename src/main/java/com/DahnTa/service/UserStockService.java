package com.DahnTa.service;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.DahnTa.entity.Interest;
import com.DahnTa.entity.Stock;
import com.DahnTa.entity.User;
import com.DahnTa.repository.InterestRepository;
import com.DahnTa.repository.StockRepository;
import com.DahnTa.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service

public class UserStockService {
    private final JWTService jwtService;
    private final StockRepository stockRepository;
    private final InterestRepository interestRepository;
    private final UserRepository userRepository;

    public UserStockService(JWTService jwtService, StockRepository stockRepository,
    InterestRepository interestRepository, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.stockRepository = stockRepository;
        this.interestRepository = interestRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public void applyDislike(Long stockId, String bearerToken) {
        // bearer 문자 제외
        String token = bearerToken.replace("bearer", "");

        // validation token
        if (!jwtService.validateToken(token)) {
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid token");
        }

        Long userId = jwtService.extractUserId(token);

        // stock_id 조회 및 검증(에러 처리)
        stockRepository.findById(stockId).orElseThrow(
            () -> new ResponseStatusException(NOT_FOUND, "Stock not found"));


        // 해당 유저의 interest에서 stock_id 삭제(에러 처리)
        Interest interest = interestRepository.findByUserIdAndStockId(userId, stockId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Interest not found")); // 404 return

        interestRepository.delete(interest);

    }

    @Transactional
    public void applyLike(Long stockId, String bearerToken) {
        // bearer 문자 제외
        String token = bearerToken.replace("bearer", "");

        // validation token
        if (!jwtService.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }

        Long userId = jwtService.extractUserId(token);

        // stock_id 조회 및 검증(예외 처리)
          Stock stock = stockRepository.findById(stockId).orElseThrow(
            () -> new ResponseStatusException(NOT_FOUND, "Stock not found"));

        // 해당 유저의 interest에 stock_id 추가(에러 처리)
        // 이미 처리 돼 있으면 pass

        // (옵션) user 엔티티가 필요하면 로드
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));

        // 이미 관심에 있으면 conflict 또는 무시
        if (interestRepository.existsByUserIdAndStockId(userId, stockId)) {
            throw new ResponseStatusException(CONFLICT, "Already interested");
        }

        Interest interest = new Interest(user, stock);
        interestRepository.save(interest);
    }

}