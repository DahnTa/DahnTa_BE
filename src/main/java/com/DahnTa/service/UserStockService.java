package com.DahnTa.service;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.DahnTa.Mapper.TransactionMapper;
import com.DahnTa.dto.response.AssetResponseDTO;
import com.DahnTa.dto.response.HoldingsListResponseDTO;
import com.DahnTa.dto.response.HoldingsResponseDTO;
import com.DahnTa.dto.response.InterestResponseDTO;
import com.DahnTa.dto.response.TransactionListResponseDTO;
import com.DahnTa.dto.response.TransactionResponseDTO;
import com.DahnTa.entity.CurrentPrice;
import com.DahnTa.entity.Interest;
import com.DahnTa.entity.Possession;
import com.DahnTa.entity.Stock;
import com.DahnTa.entity.Transaction;
import com.DahnTa.entity.User;
import com.DahnTa.repository.CurrentPriceRepository;
import com.DahnTa.repository.InterestRepository;
import com.DahnTa.repository.PossessionRepository;
import com.DahnTa.repository.StockRepository;
import com.DahnTa.repository.TransactionRepository;
import com.DahnTa.repository.UserRepository;
import java.util.List;
import java.util.Optional;
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
    private final PossessionRepository possessionRepository;
    private final TransactionMapper transactionMapper;

    public UserStockService(JWTService jwtService, StockRepository stockRepository,
    InterestRepository interestRepository, UserRepository userRepository,
        TransactionRepository transactionRepository, CurrentPriceRepository currentPriceRepository,
        PossessionRepository possessionRepository, TransactionMapper transactionMapper) {
        this.jwtService = jwtService;
        this.stockRepository = stockRepository;
        this.interestRepository = interestRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.currentPriceRepository = currentPriceRepository;
        this.possessionRepository = possessionRepository;
        this.transactionMapper = transactionMapper;
    }


    public HoldingsListResponseDTO getHoldings(String bearerToken) {
        Long userId = extractUserIdFromToken(bearerToken);
        List<Possession> possessions = possessionRepository.findAllByUserId(userId);

        List<HoldingsResponseDTO> dtoList = possessions.stream()
            .map(p -> {
                CurrentPrice priceEntity = fetchCurrentPrice(p.getStock());

                double currentPrice = priceEntity.getCurrentPrice();
                double marketPrice = priceEntity.getMarketPrice();

                // 변동률 계산
                double changeRate = calculateChangeRate(currentPrice, marketPrice);
                // 평가금 계산
                double valuation = currentPrice * p.getQuantity();

                return new HoldingsResponseDTO(
                    p.getStock().getStockName(),
                    p.getStock().getStockTag(),
                    p.getQuantity(),
                    changeRate,
                    valuation
                );
            })
            .toList();

        return new HoldingsListResponseDTO(dtoList);
    }


    public AssetResponseDTO getAssets(String bearerToken) {
        Long userId = extractUserIdFromToken(bearerToken);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));

        double userCredit = user.getUserCredit();
        List<Possession> possessions = possessionRepository.findAllByUserId(userId);

        double stockValuation = possessions.stream()
            .mapToDouble(p -> {
                CurrentPrice price = fetchCurrentPrice(p.getStock());
                return price.getCurrentPrice() * p.getQuantity();
            })
            .sum();

        double totalAmount = userCredit + stockValuation;
        double seedMoney = 10000000.0;
        double creditChangeAmount = totalAmount - seedMoney;
        double creditChangeRate = (creditChangeAmount / seedMoney) * 100;

        return new AssetResponseDTO(
            totalAmount,
            creditChangeRate,
            creditChangeAmount,
            userCredit,
            stockValuation
        );
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

        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);

        List<TransactionResponseDTO> mapped = transactions.stream()
            .map(transactionMapper::toDTO)
            .toList();

        return new TransactionListResponseDTO(mapped);
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


    private CurrentPrice fetchCurrentPrice(Stock stock) {
        Optional<CurrentPrice> priceBox = currentPriceRepository
            .findTop1ByStockIdOrderByDateDesc(stock.getId());

        if (priceBox.isEmpty()) {
            throw new IllegalStateException("가격 정보 없음: " + stock.getStockName());
        }

        return priceBox.get();
    }

    private double calculateChangeRate(double current, double market) {
        if (market <= 0) return 0.0;
        return ((current - market) / market) * 100.0;
    }


}