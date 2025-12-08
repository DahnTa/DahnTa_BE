package com.DahnTa.service;

import com.DahnTa.Mapper.TransactionMapper;
import com.DahnTa.dto.response.AssetResponseDTO;
import com.DahnTa.dto.response.HoldingsListResponseDTO;
import com.DahnTa.dto.response.HoldingsResponseDTO;
import com.DahnTa.dto.response.InterestResponseDTO;
import com.DahnTa.dto.response.TransactionListResponseDTO;
import com.DahnTa.dto.response.TransactionResponseDTO;
import com.DahnTa.entity.CurrentPrice;
import com.DahnTa.entity.Enum.DateOffset;
import com.DahnTa.entity.Enum.ErrorCode;
import com.DahnTa.entity.Interest;
import com.DahnTa.entity.Possession;
import com.DahnTa.entity.Stock;
import com.DahnTa.entity.Transaction;
import com.DahnTa.entity.User;
import com.DahnTa.entity.saveDB.CurrentPriceSave;
import com.DahnTa.exception.UserStockException;
import com.DahnTa.repository.InterestRepository;
import com.DahnTa.repository.PossessionRepository;
import com.DahnTa.repository.SaveDBRepository.CurrentPriceSaveRepository;
import com.DahnTa.repository.StockRepository;
import com.DahnTa.repository.TransactionRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserStockService {

    private static final double SEED_MONEY = 10000.0;
    public static final double PERCENT = 100.0;

    private final StockRepository stockRepository;
    private final InterestRepository interestRepository;
    private final TransactionRepository transactionRepository;
    private final CurrentPriceSaveRepository currentPriceSaveRepository;
    private final PossessionRepository possessionRepository;
    private final TransactionMapper transactionMapper;

    public UserStockService(StockRepository stockRepository, InterestRepository interestRepository,
        TransactionRepository transactionRepository, CurrentPriceSaveRepository currentPriceSaveRepository,
        PossessionRepository possessionRepository, TransactionMapper transactionMapper) {
        this.stockRepository = stockRepository;
        this.interestRepository = interestRepository;
        this.transactionRepository = transactionRepository;
        this.currentPriceSaveRepository = currentPriceSaveRepository;
        this.possessionRepository = possessionRepository;
        this.transactionMapper = transactionMapper;
    }

    public HoldingsListResponseDTO getHoldings(User user) {
        List<Possession> possessions = possessionRepository.findAllByUser(user);

        List<HoldingsResponseDTO> dtoList = possessions.stream()
            .map(p -> {
                CurrentPriceSave priceEntity = fetchCurrentPrice(p.getStock());

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


    public AssetResponseDTO getAssets(User user) {
        double userCredit = user.getUserCredit();
        List<Possession> possessions = possessionRepository.findAllByUser(user);

        double stockValuation = possessions.stream()
            .mapToDouble(p -> {
                CurrentPriceSave price = fetchCurrentPrice(p.getStock());
                return price.getCurrentPrice() * p.getQuantity();
            })
            .sum();

        double totalAmount = userCredit + stockValuation;
        double seedMoney = SEED_MONEY;
        double creditChangeAmount = totalAmount - seedMoney;
        double creditChangeRate = (creditChangeAmount / seedMoney) * PERCENT;

        return new AssetResponseDTO(
            totalAmount,
            creditChangeRate,
            creditChangeAmount,
            userCredit,
            stockValuation
        );
    }

    public List<InterestResponseDTO> getInterestList(User user) {
        List<Interest> interests = interestRepository.findAllByUser(user);

        return interests.stream()
            .map(interest -> {
                Stock stock = interest.getStock();

                List<CurrentPriceSave> prices =
                    currentPriceSaveRepository.findTop2ByStockIdOrderByDateDesc(stock.getId());

                double today = prices.get(0).getCurrentPrice();
                double changeRate = 0.0;

                // 어제 대비 변동률 계산(최신순으로 정렬 돼 있음)
                if (prices.size() > 1) {
                    double yesterday = prices.get(DateOffset.PREVIOUS_DAY.getDays()).getCurrentPrice();
                    changeRate = ((today - yesterday) / yesterday) * PERCENT;
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
    public void applyDislike(Long stockId, User user) {
        validateStock(stockId);

        Interest interest = interestRepository.findByUserAndStockId(user, stockId)
            .orElseThrow(() -> new UserStockException(ErrorCode.INTEREST_NOT_FOUND));

        interestRepository.delete(interest);
    }


    @Transactional
    public void applyLike(Long stockId, User user) {
        Stock stock = validateStock(stockId);

        if (interestRepository.existsByUserAndStockId(user, stockId)) {
            throw new UserStockException(ErrorCode.ALREADY_INTERESTED);
        }

        Interest interest = new Interest(user, stock);
        interestRepository.save(interest);
    }

    public TransactionListResponseDTO getTransactionHistory(User user) {
        List<Transaction> transactions = transactionRepository.findAllByUser(user);

        List<TransactionResponseDTO> mapped = transactions.stream()
            .map(transactionMapper::toDTO)
            .toList();

        return new TransactionListResponseDTO(mapped);
    }

    private Stock validateStock(Long stockId) {
        return stockRepository.findById(stockId)
            .orElseThrow(() -> new UserStockException(ErrorCode.STOCK_NOT_FOUND));
    }


    private CurrentPriceSave fetchCurrentPrice(Stock stock) {
        return currentPriceSaveRepository.findTop1ByStockIdOrderByDateDesc(stock.getId())
            .orElseThrow(() -> new UserStockException(ErrorCode.PRICE_INFO_NOT_FOUND));
    }

    private double calculateChangeRate(double current, double market) {
        if (market <= 0) return 0.0;
        return ((current - market) / market) * PERCENT;
    }
}