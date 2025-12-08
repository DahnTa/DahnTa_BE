package com.DahnTa.service;

import com.DahnTa.dto.DashBoard;
import com.DahnTa.dto.MarketPrices;
import com.DahnTa.dto.request.StockBuyRequest;
import com.DahnTa.dto.request.StockSellRequest;
import com.DahnTa.dto.response.MacroIndicatorsResponse;
import com.DahnTa.dto.response.StockCompanyFinanceResponse;
import com.DahnTa.dto.response.StockGameResultResponse;
import com.DahnTa.dto.response.StockListResponse;
import com.DahnTa.dto.response.StockNewsResponse;
import com.DahnTa.dto.response.StockOrderResponse;
import com.DahnTa.dto.response.StockRedditResponse;
import com.DahnTa.dto.response.StockResponse;
import com.DahnTa.dto.response.StockTotalAnalysisResponse;
import com.DahnTa.entity.CompanyFinance;
import com.DahnTa.entity.CurrentPrice;
import com.DahnTa.entity.Enum.DateOffset;
import com.DahnTa.entity.Enum.ErrorCode;
import com.DahnTa.entity.Enum.GameDateSetting;
import com.DahnTa.entity.Enum.TransactionType;
import com.DahnTa.entity.GameDate;
import com.DahnTa.entity.MacroIndicators;
import com.DahnTa.entity.News;
import com.DahnTa.entity.Possession;
import com.DahnTa.entity.Reddit;
import com.DahnTa.entity.Stock;
import com.DahnTa.entity.TotalAnalysis;
import com.DahnTa.entity.Transaction;
import com.DahnTa.entity.User;
import com.DahnTa.entity.saveDB.CompanyFinanceSave;
import com.DahnTa.entity.saveDB.CurrentPriceSave;
import com.DahnTa.entity.saveDB.MacroIndicatorsSave;
import com.DahnTa.entity.saveDB.NewsSave;
import com.DahnTa.entity.saveDB.RedditSave;
import com.DahnTa.entity.saveDB.TotalAnalysisSave;
import com.DahnTa.exception.StockException;
import com.DahnTa.repository.CompanyFinanceRepository;
import com.DahnTa.repository.CurrentPriceRepository;
import com.DahnTa.repository.GameDateRepository;
import com.DahnTa.repository.MacroIndicatorsRepository;
import com.DahnTa.repository.NewsRepository;
import com.DahnTa.repository.PossessionRepository;
import com.DahnTa.repository.RedditRepository;
import com.DahnTa.repository.SaveDBRepository.CompanyFinanceSaveRepository;
import com.DahnTa.repository.SaveDBRepository.CurrentPriceSaveRepository;
import com.DahnTa.repository.SaveDBRepository.MacroIndicatorsSaveRepository;
import com.DahnTa.repository.SaveDBRepository.NewsSaveRepository;
import com.DahnTa.repository.SaveDBRepository.RedditSaveRepository;
import com.DahnTa.repository.SaveDBRepository.TotalAnalysisSaveRepository;
import com.DahnTa.repository.StockRepository;
import com.DahnTa.repository.TotalAnalysisRepository;
import com.DahnTa.repository.TransactionRepository;
import com.DahnTa.util.CsvLoadUtil;
import com.DahnTa.util.RemoveGameDataUtil;
import com.DahnTa.util.SaveDBLoadUtil;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StockService {

    private static final double INITIAL_FUNDS = 10000.0;
    private static final int ONE_TRANSACTION = 1;

    private final GameDateRepository gameDateRepository;
    private final StockRepository stockRepository;
    private final PossessionRepository possessionRepository;
    private final CurrentPriceRepository currentPriceRepository;
    private final CompanyFinanceRepository companyFinanceRepository;
    private final MacroIndicatorsRepository macroIndicatorsRepository;
    private final NewsRepository newsRepository;
    private final RedditRepository redditRepository;
    private final TotalAnalysisRepository totalAnalysisRepository;
    private final TransactionRepository transactionRepository;
    private final SaveDBLoadUtil saveDBLoadUtil;
    private final RemoveGameDataUtil removeGameDataUtil;
    private final CurrentPriceSaveRepository currentPriceSaveRepository;
    private final CompanyFinanceSaveRepository companyFinanceSaveRepository;
    private final MacroIndicatorsSaveRepository macroIndicatorsSaveRepository;
    private final NewsSaveRepository newsSaveRepository;
    private final RedditSaveRepository redditSaveRepository;
    private final TotalAnalysisSaveRepository totalAnalysisSaveRepository;

    public StockService(GameDateRepository gameDateRepository, StockRepository stockRepository,
        PossessionRepository possessionRepository, CurrentPriceRepository currentPriceRepository,
        CompanyFinanceRepository companyFinanceRepository,
        MacroIndicatorsRepository macroIndicatorsRepository, NewsRepository newsRepository,
        RedditRepository redditRepository, TotalAnalysisRepository totalAnalysisRepository,
        TransactionRepository transactionRepository,
        SaveDBLoadUtil saveDBLoadUtil, RemoveGameDataUtil removeGameDataUtil,

        CurrentPriceSaveRepository currentPriceSaveRepository,
        CompanyFinanceSaveRepository companyFinanceSaveRepository,
        MacroIndicatorsSaveRepository macroIndicatorsSaveRepository,
        NewsSaveRepository newsSaveRepository,
        RedditSaveRepository redditSaveRepository,
        TotalAnalysisSaveRepository totalAnalysisSaveRepository) {
        this.gameDateRepository = gameDateRepository;
        this.stockRepository = stockRepository;
        this.possessionRepository = possessionRepository;
        this.currentPriceRepository = currentPriceRepository;
        this.companyFinanceRepository = companyFinanceRepository;
        this.macroIndicatorsRepository = macroIndicatorsRepository;
        this.newsRepository = newsRepository;
        this.redditRepository = redditRepository;
        this.totalAnalysisRepository = totalAnalysisRepository;
        this.transactionRepository = transactionRepository;
        this.saveDBLoadUtil = saveDBLoadUtil;
        this.removeGameDataUtil = removeGameDataUtil;

        this.currentPriceSaveRepository = currentPriceSaveRepository;
        this.companyFinanceSaveRepository = companyFinanceSaveRepository;
        this.macroIndicatorsSaveRepository = macroIndicatorsSaveRepository;
        this.newsSaveRepository = newsSaveRepository;
        this.redditSaveRepository = redditSaveRepository;
        this.totalAnalysisSaveRepository = totalAnalysisSaveRepository;
    }

    public void gameStart(User user) {
        LocalDate start = GameDateSetting.START.getDateValue();
        LocalDate end = GameDateSetting.END.getDateValue();
        int dayDuration = GameDateSetting.DAY_DURATION.getNumberValue();

        long maxOffset = ChronoUnit.DAYS.between(start, end) - dayDuration;
        long randomOffset = ThreadLocalRandom.current().nextLong(0, maxOffset + 1);

        LocalDate randomStart = start.plusDays(randomOffset);
        LocalDate randomEnd = randomStart.plusDays(dayDuration - 1);

        GameDate gameDate = GameDate.create(user, randomStart, randomEnd,
            GameDateSetting.START_DAY.getNumberValue());
        gameDateRepository.save(gameDate);
    }

    public void gameDateNext(User user) {
        GameDate gameDate = getGameDateByUser(user);
        gameDate.updateDay();
    }

    public void gameFinish(User user) {
        removeGameDataByUser(user);
    }

    public void stockBuy(User user, Long stockId, StockBuyRequest request) {
        Stock stock = getStockByStockId(stockId);
        LocalDate today = getToday(user);
        CurrentPriceSave currentPrice = getCurrentPriceByStockAndDate(stock, today);

        currentPrice.validateBuyQuantity(user.getUserCredit(), request.getQuantity());

        Possession possession = getPossessionByStockAndUser(stock, user);
        if (possession == null) {
            possession = Possession.create(stock, user, 0);
            possessionRepository.save(possession);
        }
        possession.increaseQuantity(request.getQuantity());
        user.deductCredit(currentPrice.getCurrentPrice() * request.getQuantity());
    }

    public void stockSell(User user, Long stockId, StockSellRequest request) {
        Stock stock = getStockByStockId(stockId);
        LocalDate today = getToday(user);

        if (!possessionRepository.existsByStockAndUser(stock, user)) {
            throw new StockException(ErrorCode.POSSESSION_NOT_FOUND);
        }
        Possession possession = getPossessionByStockAndUser(stock, user);
        possession.validateSellQuantity(request.getQuantity());

        possession.decrementQuantity(request.getQuantity());
        if (possession.getQuantity() == 0) {
            possessionRepository.delete(possession);
        }

        CurrentPriceSave currentPrice = getCurrentPriceByStockAndDate(stock, today);
        user.increaseCredit(currentPrice.getCurrentPrice() * request.getQuantity());
    }

    public StockListResponse getStockList(User user) {
        List<Stock> stocks = stockRepository.findAll();
        LocalDate today = getToday(user);

        List<DashBoard> dashBoards = new ArrayList<>();
        for (Stock stock : stocks) {
            CurrentPriceSave currentPrice = getCurrentPriceByStockAndDate(stock, today);
            DashBoard board = DashBoard.create(stock.getId(), stock.getStockName(), stock.getStockTag(),
                currentPrice.getCurrentPrice(), currentPrice.getMarketPrice(),
                getChangeRate(stock, currentPrice, today), getChangeAmount(stock, currentPrice, today));
            dashBoards.add(board);
        }

        return StockListResponse.create(dashBoards);
    }

    public StockResponse getStock(User user, Long stockId) {
        Stock stock = getStockByStockId(stockId);
        System.out.println("stockId:" + stock.getId());
        LocalDate today = getToday(user);
        System.out.println("today:" + today);
        CurrentPriceSave currentPrice = getCurrentPriceByStockAndDate(stock, today);
        GameDate gameDate = getGameDateByUser(user);
        List<MarketPrices> marketPrices = getMarketPricesUntilToday(stock, gameDate.getStartDate(), today);

        return StockResponse.create(stock.getStockName(), stock.getStockTag(), marketPrices,
            currentPrice.getCurrentPrice(), getChangeRate(stock, currentPrice, today));
    }

    public StockOrderResponse getStockOrder(User user, Long stockId) {
        int quantity = 0;
        Stock stock = getStockByStockId(stockId);
        Possession possession = getPossessionByStockAndUser(stock, user);
        if (possession != null) {
            quantity = possession.getQuantity();
        }

        LocalDate today = getToday(user);
        CurrentPriceSave currentPrice = getCurrentPriceByStockAndDate(stock, today);
        double averagePrice = getAveragePrice(stock, user);

        return StockOrderResponse.create(quantity, averagePrice,
            currentPrice.calculateAvailableOrderAmount(user.getUserCredit()));
    }

    public StockNewsResponse getStockNews(User user, Long stockId) {
        Stock stock = getStockByStockId(stockId);
        LocalDate today = getToday(user);

        NewsSave news = getNewsByStockAndDate(stock, today);

        return StockNewsResponse.create(news.getDate(), news.getContent());
    }

    public StockCompanyFinanceResponse getStockCompanyFinance(User user, Long stockId) {
        Stock stock = getStockByStockId(stockId);
        LocalDate today = getToday(user);

        CompanyFinanceSave companyFinance = getCompanyFinanceByStockAndDate(stock, today);

        return StockCompanyFinanceResponse.create(companyFinance.getDate(), companyFinance.getContent());
    }

    public MacroIndicatorsResponse getMacroIndicators(User user) {
        LocalDate today = getToday(user);

        MacroIndicatorsSave macroIndicators = getMacroIndicatorsByStockAndDate(today);

        return MacroIndicatorsResponse.create(macroIndicators.getDate(), macroIndicators.getContent());
    }

    public StockRedditResponse getReddit(User user, Long stockId) {
        Stock stock = getStockByStockId(stockId);
        LocalDate today = getToday(user);

        RedditSave reddit = getRedditByStockAndDate(stock, today);

        return StockRedditResponse.create(reddit.getDate(), reddit.getContent(), reddit.getScore(),
            reddit.getNumComment());
    }

    public StockTotalAnalysisResponse getTotalAnalysis(User user, Long stockId) {
        Stock stock = getStockByStockId(stockId);
        LocalDate today = getToday(user);

        TotalAnalysisSave totalAnalysis = getTotalAnalysisByStockAndDate(stock, today);

        return StockTotalAnalysisResponse.create(totalAnalysis.getDate(), totalAnalysis.getCompanyName(),
            totalAnalysis.getAnalyze());
    }

    public StockGameResultResponse gatGameResult(User user) {
        double totalReturnRate = 0;
        List<Possession> possessions = getPossessionByUser(user);
        LocalDate today = getToday(user);
        for (Possession possession : possessions) {
            CurrentPriceSave currentPrice = getCurrentPriceByStockAndDate(possession.getStock(), today);
            totalReturnRate += possession.calculateAmount(currentPrice.getCurrentPrice());
        }

        double finalAmount = user.calculateFinalAmount(totalReturnRate);
        double finalReturnRate = user.calculateReturnRate(finalAmount, INITIAL_FUNDS);

        return StockGameResultResponse.create(finalReturnRate, INITIAL_FUNDS, finalAmount);
    }

    private void setGameDataByUser(User user, LocalDate randomStart, LocalDate randomEnd) {
        saveDBLoadUtil.loadSaveDBForCurrentPrice(user, randomStart, randomEnd);
        saveDBLoadUtil.loadSaveDBForNews(user, randomStart, randomEnd);
        saveDBLoadUtil.loadSaveDBForMacroIndicators(user, randomStart, randomEnd);
        saveDBLoadUtil.loadSaveDBForCompanyFinance(user, randomStart, randomEnd);
        saveDBLoadUtil.loadSaveDBForTotalAnalysis(user, randomStart, randomEnd);
        saveDBLoadUtil.loadSaveDBForReddit(user, randomStart, randomEnd);
    }

    private void removeGameDataByUser(User user) {
        removeGameDataUtil.gameDateRemoveByTransaction(user);
        removeGameDataUtil.gameDateRemoveByInterest(user);
        removeGameDataUtil.gameDataRemoveByPossession(user);

        removeGameDataUtil.gameDataRemoveByCurrentPrice(user);
        removeGameDataUtil.gameDataRemoveByCompanyFinance(user);
        removeGameDataUtil.gameDataRemoveByMacroIndicators(user);
        removeGameDataUtil.gameDataRemoveByNews(user);
        removeGameDataUtil.gameDataRemoveByReddit(user);
        removeGameDataUtil.gameDataRemoveByTotalAnalysis(user);

        removeGameDataUtil.gameDataRemoveByGameDate(user);
    }

    private double getChangeRate(Stock stock, CurrentPriceSave currentPrice, LocalDate date) {
        CurrentPriceSave yesterdayPrice = getCurrentPriceByStockAndDate(stock, date.minusDays(
            DateOffset.PREVIOUS_DAY.getDays()));
        return currentPrice.calculateChangeRate(yesterdayPrice);
    }

    private double getChangeAmount(Stock stock, CurrentPriceSave currentPrice, LocalDate date) {
        CurrentPriceSave yesterdayPrice = getCurrentPriceByStockAndDate(stock,
            date.minusDays(DateOffset.PREVIOUS_DAY.getDays()));
        return currentPrice.calculateChangeAmount(yesterdayPrice);
    }

    private LocalDate getToday(User user) {
        GameDate gameDate = getGameDateByUser(user);
        int day = gameDate.getDay();
        LocalDate startDate = gameDate.getStartDate();

        return startDate.plusDays(day + 10);
    }

    private List<MarketPrices> getMarketPricesUntilToday(Stock stock, LocalDate startDate, LocalDate today) {
        List<MarketPrices> marketPrices = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(today);
            date = date.plusDays(DateOffset.NEXT_DAY.getDays())) {
            CurrentPriceSave currentPrice = getCurrentPriceByStockAndDate(stock, date);
            marketPrices.add(MarketPrices.create(currentPrice.getMarketPrice()));
        }

        return marketPrices;
    }

    private double getAveragePrice(Stock stock, User user) {
        int transactionCount = 0;
        double totalAmount = 0;

        List<Transaction> transactions = transactionRepository.findByStockAndUser(stock, user);
        for (Transaction transaction : transactions) {
            if (transaction.getType().equals(TransactionType.BUY)) {
                totalAmount += transaction.getTotalAmount();
                transactionCount += ONE_TRANSACTION;
            }
        }

        return totalAmount / transactionCount;
    }

    private Stock getStockByStockId(Long stockId) {

        return stockRepository.findById(stockId)
            .orElseThrow(() -> new StockException(ErrorCode.STOCK_NOT_FOUND));
    }

    private GameDate getGameDateByUser(User user) {

        return gameDateRepository.findByUser(user)
            .orElseThrow(() -> new StockException(ErrorCode.GAME_DATE_NOT_FOUND));
    }

    private Possession getPossessionByStockAndUser(Stock stock, User user) {

        return possessionRepository.findByStockAndUser(stock, user)
            .orElse(null);
    }

    private CurrentPriceSave getCurrentPriceByStockAndDate(Stock stock, LocalDate date) {

        return currentPriceSaveRepository.findFirstByStockIdAndDateLessThanEqualOrderByDateDesc(stock.getId(), date)
            .orElseThrow(() -> new StockException(ErrorCode.PRICE_NOT_FOUND));
    }

    private NewsSave getNewsByStockAndDate(Stock stock, LocalDate date) {
        return newsSaveRepository.findByStockIdAndDate(stock.getId(), date)
            .orElseThrow(() -> new StockException(ErrorCode.NEWS_NOT_FOUND));
    }

    private CompanyFinanceSave getCompanyFinanceByStockAndDate(Stock stock, LocalDate date) {
        return companyFinanceSaveRepository.findByStockIdAndDate(stock.getId(), date)
            .orElseThrow(() -> new StockException(ErrorCode.COMPANY_FINANCE_NOT_FOUND));
    }

    private MacroIndicatorsSave getMacroIndicatorsByStockAndDate(LocalDate date) {
        return macroIndicatorsSaveRepository.findByDate(date)
            .orElseThrow(() -> new StockException(ErrorCode.MACRO_INDICATORS_NOT_FOUND));
    }

    private RedditSave getRedditByStockAndDate(Stock stock, LocalDate date) {
        return redditSaveRepository.findByStockIdAndDate(stock.getId(), date)
            .orElseThrow(() -> new StockException(ErrorCode.REDDIT_NOT_FOUND));
    }

    private TotalAnalysisSave getTotalAnalysisByStockAndDate(Stock stock, LocalDate date) {
        return totalAnalysisSaveRepository.findByStockIdAndDate(stock.getId(), date)
            .orElseThrow(() -> new StockException(ErrorCode.TOTAL_ANALYSIS_NOT_FOUND));
    }

    private List<Possession> getPossessionByUser(User user) {

        return possessionRepository.findByUser(user);
    }
}
