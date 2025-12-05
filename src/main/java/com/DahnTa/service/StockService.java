package com.DahnTa.service;

import com.DahnTa.dto.DashBoard;
import com.DahnTa.dto.MarketPrices;
import com.DahnTa.dto.request.StockBuyRequest;
import com.DahnTa.dto.response.MacroIndicatorsResponse;
import com.DahnTa.dto.response.StockCompanyFinanceResponse;
import com.DahnTa.dto.response.StockListResponse;
import com.DahnTa.dto.response.StockNewsResponse;
import com.DahnTa.dto.response.StockOrderResponse;
import com.DahnTa.dto.response.StockRedditResponse;
import com.DahnTa.dto.response.StockResponse;
import com.DahnTa.dto.response.StockTotalAnalysisResponse;
import com.DahnTa.entity.CompanyFinance;
import com.DahnTa.entity.CurrentPrice;
import com.DahnTa.entity.GameDate;
import com.DahnTa.entity.MacroIndicators;
import com.DahnTa.entity.News;
import com.DahnTa.entity.Possession;
import com.DahnTa.entity.Reddit;
import com.DahnTa.entity.Stock;
import com.DahnTa.entity.TotalAnalysis;
import com.DahnTa.entity.User;
import com.DahnTa.repository.CompanyFinanceRepository;
import com.DahnTa.repository.CurrentPriceRepository;
import com.DahnTa.repository.GameDateRepository;
import com.DahnTa.repository.MacroIndicatorsRepository;
import com.DahnTa.repository.NewsRepository;
import com.DahnTa.repository.PossessionRepository;
import com.DahnTa.repository.RedditRepository;
import com.DahnTa.repository.StockRepository;
import com.DahnTa.repository.TotalAnalysisRepository;
import com.DahnTa.util.CsvLoadUtil;
import com.DahnTa.util.RemoveGameDataUtil;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StockService {

    private final GameDateRepository gameDateRepository;
    private final StockRepository stockRepository;
    private final PossessionRepository possessionRepository;
    private final CurrentPriceRepository currentPriceRepository;
    private final CompanyFinanceRepository companyFinanceRepository;
    private final MacroIndicatorsRepository macroIndicatorsRepository;
    private final NewsRepository newsRepository;
    private final RedditRepository redditRepository;
    private final TotalAnalysisRepository totalAnalysisRepository;
    private final CsvLoadUtil csvLoadUtil;
    private final RemoveGameDataUtil removeGameDataUtil;

    public StockService(GameDateRepository gameDateRepository, StockRepository stockRepository,
        PossessionRepository possessionRepository, CurrentPriceRepository currentPriceRepository,
        CompanyFinanceRepository companyFinanceRepository,
        MacroIndicatorsRepository macroIndicatorsRepository, NewsRepository newsRepository,
        RedditRepository redditRepository, TotalAnalysisRepository totalAnalysisRepository,
        CsvLoadUtil csvLoadUtil, RemoveGameDataUtil removeGameDataUtil) {
        this.gameDateRepository = gameDateRepository;
        this.stockRepository = stockRepository;
        this.possessionRepository = possessionRepository;
        this.currentPriceRepository = currentPriceRepository;
        this.companyFinanceRepository = companyFinanceRepository;
        this.macroIndicatorsRepository = macroIndicatorsRepository;
        this.newsRepository = newsRepository;
        this.redditRepository = redditRepository;
        this.totalAnalysisRepository = totalAnalysisRepository;
        this.csvLoadUtil = csvLoadUtil;
        this.removeGameDataUtil = removeGameDataUtil;
    }

    public void gameStart() {
        LocalDate start = LocalDate.of(2024, 10, 1);
        LocalDate end = LocalDate.of(2025, 10, 1);
        LocalDate lastestStart = end.minusDays(29);

        long days = ChronoUnit.DAYS.between(start, lastestStart);
        long randomOffset = ThreadLocalRandom.current().nextLong(0, days + 1);

        LocalDate randomStart = start.plusDays(randomOffset);
        LocalDate randomEnd = randomStart.plusDays(19);

        GameDate gameDate = GameDate.create(user, randomStart, randomEnd, 1);
        gameDateRepository.save(gameDate);

        setGameDataByUser(user, randomStart, randomEnd);
    }

    public void gameDateNext() {
        GameDate gameDate = getGameDateByUser(user);
        gameDate.updateDay();
    }

    public void gameFinish() {
        removeGameDataByUser(user);
    }

    public void stockBuy(Long stockId, StockBuyRequest request) {
        Stock stock = getStockByStockId(stockId);
        LocalDate today = getToday(user);
        CurrentPrice currentPrice = getCurrentPriceByStockAndDate(stock, today);

        currentPrice.validateBuyQuantity(user.getUserCredit, request.getQuantity());

        Possession possession = getPossessionByStockAndUser(stock, user);
        if (possession == null) {
            possession = Possession.create(stock, user, 0);
            possessionRepository.save(possession);
        }
        possession.increaseQuantity(request.getQuantity());

        /*
        user.deductCredit(currentPrice.getCurrentPrice*request.getQuantity());
        ┎─────────────────────────────────────────────┐
            User 도메인에 작성 ↓
            public void deductCredit(int amount) {
                this.credit -= amount;
            }
        └─────────────────────────────────────────────┘
         */
    }

    public void stockSell(Long stockId, StockBuyRequest request) {
        Stock stock = getStockByStockId(stockId);
        LocalDate today = getToday(user);

        if (!possessionRepository.existsByStockAnUser(stock, user)) {
            throw new IllegalArgumentException("해당 주식을 보유하고 있지 않습니다.");
        }
        Possession possession = getPossessionByStockAndUser(stock, user);
        possession.validateSellQuantity(request.getQuantity());

        possession.decrementQuantity(request.getQuantity());
        if (possession.getQuantity() == 0) {
            possessionRepository.delete(possession);
        }

        CurrentPrice currentPrice = getCurrentPriceByStockAndDate(stock, today);
        /*
        user.increaseCredit(currentPrice.getCurrentPrice*getQuantity.quantity());
        ┎─────────────────────────────────────────────┐
            User 도메인에 작성 ↓
            public void increaseCredit(int amount) {
                this.credit += amount;
            }
        └─────────────────────────────────────────────┘
         */
    }

    public StockListResponse getStockList() {
        List<Stock> stocks = stockRepository.findAll();
        LocalDate today = getToday(user);

        List<DashBoard> dashBoards = new ArrayList<>();
        for (Stock stock : stocks) {
            CurrentPrice currentPrice = getCurrentPriceByStockAndDate(stock, today);
            DashBoard board = DashBoard.create(stock.getId(), stock.getStockName(), stock.getStockTag(),
                currentPrice.getCurrentPrice(), currentPrice.getMarketPrice(),
                getChangeRate(stock, currentPrice, today), getChangeAmount(stock, currentPrice, today));
            dashBoards.add(board);
        }

        return StockListResponse.create(dashBoards);
    }

    public StockResponse getStock(Long stockId) {
        Stock stock = getStockByStockId(stockId);
        LocalDate today = getToday(user);
        CurrentPrice currentPrice = getCurrentPriceByStockAndDate(stock, today);
        GameDate gameDate = getGameDateByUser(user);
        List<MarketPrices> marketPrices = getMarketPricesUntilToday(stock, gameDate.getStartDate(), today);

        return StockResponse.create(stock.getStockName(), stock.getStockTag(), marketPrices,
            currentPrice.getCurrentPrice(), getChangeRate(stock, currentPrice, today));
    }

    public StockOrderResponse getStockOrder(Long stockId) {
        int quantity = 0;
        Stock stock = getStockByStockId(stockId);
        Possession possession = getPossessionByStockAndUser(stock, user);
        if (possession != null) {
            quantity = possession.getQuantity();
        }

        LocalDate today = getToday(user);
        CurrentPrice currentPrice = getCurrentPriceByStockAndDate(stock, today);

        return StockOrderResponse.create(quantity, ,
            currentPrice.calculateAvailableOrderAmount(user.getUserCredit));
    }

    public StockNewsResponse getStockNews(Long stockId) {
        Stock stock = getStockByStockId(stockId);
        LocalDate today = getToday(user);

        News news = getNewsByStockAndDate(stock, today);

        return StockNewsResponse.create(news.getDate(), news.getDisclaimer(), news.getContent());
    }

    public StockCompanyFinanceResponse getStockCompanyFinance(Long stockId) {
        Stock stock = getStockByStockId(stockId);
        LocalDate today = getToday(user);

        CompanyFinance companyFinance = getCompanyFinanceByStockAndDate(stock, today);

        return StockCompanyFinanceResponse.create(companyFinance.getDate(), companyFinance.getDisclaimer(),
            companyFinance.getContent());
    }

    public MacroIndicatorsResponse getMacroIndicators() {
        LocalDate today = getToday(user);

        MacroIndicators macroIndicators = getMacroIndicatorsByStockAndDate(today);

        return MacroIndicatorsResponse.create(macroIndicators.getDate(), macroIndicators.getDisclaimer(),
            macroIndicators.getContent());
    }

    public StockRedditResponse getReddit(Long stockId) {
        Stock stock = getStockByStockId(stockId);
        LocalDate today = getToday(user);

        Reddit reddit = getRedditByStockAndDate(stock, today);

        return StockRedditResponse.create(reddit.getDate(), reddit.getContent(), reddit.getScore(),
            reddit.getNumComment());
    }

    public StockTotalAnalysisResponse getTotalAnalysis(Long stockId) {
        Stock stock = getStockByStockId(stockId);
        LocalDate today = getToday(user);

        TotalAnalysis totalAnalysis = getTotalAnalysisByStockAndDate(stock, today);

        return StockTotalAnalysisResponse.create(totalAnalysis.getDate(), totalAnalysis.getCompanyName(),
            totalAnalysis.getAnalyze());
    }

    private void setGameDataByUser(User user, LocalDate randomStart, LocalDate randomEnd) {
        csvLoadUtil.loadCsvForCurrentPrice(user, randomStart, randomEnd);
        csvLoadUtil.loadCsvForNews(user, randomStart, randomEnd);
        csvLoadUtil.loadCsvForMacroIndicators(user, randomStart, randomEnd);
        csvLoadUtil.loadCsvForCompanyFinance(user, randomStart, randomEnd);
        csvLoadUtil.loadCsvForReddit(user, randomStart, randomEnd);
        csvLoadUtil.loadCsvForTotalAnalysis(user, randomStart, randomEnd);
    }

    private void removeGameDataByUser(User user) {
        removeGameDataUtil.gameDataRemoveByCurrentPrice(user);
        removeGameDataUtil.gameDataRemoveByCompanyFinance(user);
        removeGameDataUtil.gameDataRemoveByMacroIndicators(user);
        removeGameDataUtil.gameDataRemoveByNews(user);
        removeGameDataUtil.gameDataRemoveByReddit(user);
        removeGameDataUtil.gameDataRemoveByTotalAnalysis(user);
        removeGameDataUtil.gameDataRemoveByPossession(user);
        removeGameDataUtil.gameDataRemoveByGameDate(user);
        // 관심종목, 거래내역 삭제
    }

    private double getChangeRate(Stock stock, CurrentPrice currentPrice, LocalDate date) {
        CurrentPrice yesterdayPrice = getCurrentPriceByStockAndDate(stock, date.minusDays(1));
        return currentPrice.calculateChangeRate(yesterdayPrice);
    }

    private int getChangeAmount(Stock stock, CurrentPrice currentPrice, LocalDate date) {
        CurrentPrice yesterdayPrice = getCurrentPriceByStockAndDate(stock, date.minusDays(1));
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

        for (LocalDate date = startDate; !date.isAfter(today); date = date.plusDays(1)) {
            CurrentPrice currentPrice = getCurrentPriceByStockAndDate(stock, date);
            marketPrices.add(MarketPrices.create(currentPrice.getMarketPrice()));
        }

        return marketPrices;
    }

    private Stock getStockByStockId(Long stockId) {

        return stockRepository.findById(stockId)
            .orElseThrow(() -> new IllegalArgumentException("해당 id의 stock을 찾을 수 없습니다."));
    }

    private GameDate getGameDateByUser(User user) {

        return gameDateRepository.findByUser(user)
            .orElseThrow(() -> new IllegalArgumentException("해당 user의 GameDate를 찾을 수 없습니다."));
    }

    private Possession getPossessionByStockAndUser(Stock stock, User user) {

        return possessionRepository.findByStockAndUser(stock, user)
            .orElse(null);
    }

    private CurrentPrice getCurrentPriceByStockAndDate(Stock stock, LocalDate date) {

        return currentPriceRepository.findByStockAndDate(stock, date)
            .orElseThrow(() -> new IllegalArgumentException("해당 날짜의 주식 가격을 찾을 수 없습니다."));
    }

    private News getNewsByStockAndDate(Stock stock, LocalDate date){
        return newsRepository.findByStockAndDate(stock, date)
            .orElseThrow(() -> new IllegalArgumentException("해당 날짜의 주식 뉴스를 찾을 수 없습니다."));
    }

    private CompanyFinance getCompanyFinanceByStockAndDate(Stock stock, LocalDate date){
        return companyFinanceRepository.findByStockAndDate(stock, date)
            .orElseThrow(() -> new IllegalArgumentException("해당 날짜의 주식 재무제표를 찾을 수 없습니다."));
    }

    private MacroIndicators getMacroIndicatorsByStockAndDate(LocalDate date){
        return macroIndicatorsRepository.findByDate(date)
            .orElseThrow(() -> new IllegalArgumentException("해당 날짜의 거시경제를 찾을 수 없습니다."));
    }

    private Reddit getRedditByStockAndDate(Stock stock, LocalDate date){
        return redditRepository.findByStockAndDate(stock, date)
            .orElseThrow(() -> new IllegalArgumentException("해당 날짜의 주식 Reddit을 찾을 수 없습니다."));
    }

    private TotalAnalysis getTotalAnalysisByStockAndDate(Stock stock, LocalDate date){
        return totalAnalysisRepository.findByStockAndDate(stock, date)
            .orElseThrow(() -> new IllegalArgumentException("해당 날짜의 주식 종합분석을 찾을 수 없습니다."));
    }
}
