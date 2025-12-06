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
        Long userId = extractUserIdFromToken(bearerToken);

        validateStock(stockId);

        Interest interest = interestRepository.findByUserIdAndStockId(userId, stockId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Interest not found"));

        interestRepository.delete(interest);
    }

    @Transactional
    public void applyLike(Long stockId, String bearerToken) {
        Long userId = extractUserIdFromToken(bearerToken);

        Stock stock = validateStock(stockId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));

        if (interestRepository.existsByUserIdAndStockId(userId, stockId)) {
            throw new ResponseStatusException(CONFLICT, "Already interested");
        }

        Interest interest = new Interest(user, stock);
        interestRepository.save(interest);
    }

    private Long extractUserIdFromToken(String bearerToken) {
        String token = bearerToken;
        if (bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        if (!jwtService.validateToken(token)) {
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid token");
        }
        return jwtService.extractUserId(token);
    }

    private Stock validateStock(Long stockId) {
        return stockRepository.findById(stockId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Stock not found"));
    }

}