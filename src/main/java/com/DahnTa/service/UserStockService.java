package com.DahnTa.service;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.DahnTa.dto.response.InterestResponseDTO;
import com.DahnTa.dto.response.TransactionListResponseDTO;
import com.DahnTa.dto.response.TransactionResponseDTO;
import com.DahnTa.entity.CurrentPrice;
import com.DahnTa.entity.Interest;
import com.DahnTa.entity.Stock;
import com.DahnTa.entity.Transaction;
import com.DahnTa.entity.User;
import com.DahnTa.repository.CurrentPriceRepository;
import com.DahnTa.repository.InterestRepository;
import com.DahnTa.repository.StockRepository;
import com.DahnTa.repository.TransactionRepository;
import com.DahnTa.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserStockService {
    private final JWTService jwtService;
    private final StockRepository stockRepository;
    private final InterestRepository interestRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final CurrentPriceRepository currentPriceRepository;

    public UserStockService(JWTService jwtService, StockRepository stockRepository,
    InterestRepository interestRepository, UserRepository userRepository,
        TransactionRepository transactionRepository, CurrentPriceRepository currentPriceRepository) {
        this.jwtService = jwtService;
        this.stockRepository = stockRepository;
        this.interestRepository = interestRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.currentPriceRepository = currentPriceRepository;
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

    public TransactionListResponseDTO getTransactionHistory(String bearerToken) {
        Long userId = extractUserIdFromToken(bearerToken);

        List<Transaction> transactions =
            transactionRepository.findAllByUserId(userId);

        List<TransactionResponseDTO> mapped = transactions.stream()
            .map(TransactionResponseDTO::from)
            .toList();

        return new TransactionListResponseDTO(mapped);
    }

    public List<InterestResponseDTO> getInterestList(String bearerToken) {
        Long userId = extractUserIdFromToken(bearerToken);

        List<Interest> interests = interestRepository.findAllByUserId(userId);

        return interests.stream()
            .map(interest -> {
                Stock stock = interest.getStock();

                List<CurrentPrice> prices =
                    currentPriceRepository.findTop2ByStockIdOrderByDateDesc(stock.getId());

                double today = prices.get(0).getCurrentPrice();
                double changeRate = 0.0;

                // 어제 대비 변동률 계산(최신순으로 정렬 돼 있음)
                if (prices.size() > 1) {
                    double yesterday = prices.get(1).getCurrentPrice();
                    changeRate = ((today - yesterday) / yesterday) * 100;
                }

                return new InterestResponseDTO(
                    stock.getId(),
                    stock.getStockName(),
                    stock.getStockTag(),
                    today,
                    changeRate
                );
            })
            .toList();

    }


    // private method
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