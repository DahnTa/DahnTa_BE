package com.DahnTa.service;

import com.DahnTa.dto.DashBoard;
import com.DahnTa.dto.MarketPrices;
import com.DahnTa.dto.response.StockCompanyFinanceResponse;
import com.DahnTa.dto.response.StockListResponse;
import com.DahnTa.dto.response.StockNewsResponse;
import com.DahnTa.dto.response.StockOrderResponse;
import com.DahnTa.dto.response.StockResponse;
import com.DahnTa.entity.CompanyFinance;
import com.DahnTa.entity.CurrentPrice;
import com.DahnTa.entity.GameDate;
import com.DahnTa.entity.News;
import com.DahnTa.entity.Possession;
import com.DahnTa.entity.Stock;
import com.DahnTa.repository.CompanyFinanceRepository;
import com.DahnTa.repository.CurrentPriceRepository;
import com.DahnTa.repository.GameDateRepository;
import com.DahnTa.repository.MacroIndicatorsRepository;
import com.DahnTa.repository.NewsRepository;
import com.DahnTa.repository.PossessionRepository;
import com.DahnTa.repository.RedditRepository;
import com.DahnTa.repository.StockRepository;
import com.DahnTa.unit.CsvLoadUtil;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private final GameDateRepository gameDateRepository;
    private final StockRepository stockRepository;
    private final PossessionRepository possessionRepository;
    private final CurrentPriceRepository currentPriceRepository;
    private final CompanyFinanceRepository companyFinanceRepository;
    private final MacroIndicatorsRepository macroIndicatorsRepository;
    private final NewsRepository newsRepository;
    private final RedditRepository redditRepository;
    private final CsvLoadUtil csvLoadUtil;

    public StockService(GameDateRepository gameDateRepository, StockRepository stockRepository,
        PossessionRepository possessionRepository, CurrentPriceRepository currentPriceRepository,
        CompanyFinanceRepository companyFinanceRepository,
        MacroIndicatorsRepository macroIndicatorsRepository, NewsRepository newsRepository,
        RedditRepository redditRepository, CsvLoadUtil csvLoadUtil) {
        this.gameDateRepository = gameDateRepository;
        this.stockRepository = stockRepository;
        this.possessionRepository = possessionRepository;
        this.currentPriceRepository = currentPriceRepository;
        this.companyFinanceRepository = companyFinanceRepository;
        this.macroIndicatorsRepository = macroIndicatorsRepository;
        this.newsRepository = newsRepository;
        this.redditRepository = redditRepository;
        this.csvLoadUtil = csvLoadUtil;
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

        setGameInformation(user, randomStart, randomEnd);
    }

    public StockListResponse getStockList() {
        List<Stock> stocks = stockRepository.findAll();
        LocalDate today = getToday(user);

        List<DashBoard> dashBoards = new ArrayList<>();
        for (Stock stock : stocks) {
            CurrentPrice currentPrice = currentPriceRepository.findByStockAndDate(stock, today);
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
        CurrentPrice currentPrice = currentPriceRepository.findByStockAndDate(stock, today);
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
        CurrentPrice currentPrice = currentPriceRepository.findByStockAndDate(stock, today);

        return StockOrderResponse.create(quantity, ,
            currentPrice.calculateAvailableOrderAmount(user.getUserCredit));
    }

    public StockNewsResponse getStockNews(Long stockId) {
        Stock stock = getStockByStockId(stockId);
        LocalDate today = getToday(user);

        News news = newsRepository.findByStockAndDate(stock, today);

        return StockNewsResponse.create(news.getDate(), news.getDisclaimer(), news.getContent());
    }

    public StockCompanyFinanceResponse getStockCompanyFinance(Long stockId) {
        Stock stock = getStockByStockId(stockId);
        LocalDate today = getToday(user);

        CompanyFinance companyFinance = companyFinanceRepository.findByStockAndDate(stock, today);

        return StockCompanyFinanceResponse.create(companyFinance.getDate(), companyFinance.getDisclaimer(),
            companyFinance.getContent());
    }

    private void setGameInformation(User user, LocalDate randomStart, LocalDate randomEnd) {
        csvLoadUtil.loadCsvForCurrentPrice(user, randomStart, randomEnd);
        csvLoadUtil.loadCsvForNews(user, randomStart, randomEnd);
        csvLoadUtil.loadCsvForMacroIndicators(user, randomStart, randomEnd);
        csvLoadUtil.loadCsvForCompanyFinance(user, randomStart, randomEnd);
    }

    private double getChangeRate(Stock stock, CurrentPrice currentPrice, LocalDate date) {
        CurrentPrice yesterdayPrice = currentPriceRepository.findByStockAndDate(stock, date.minusDays(1));
        return currentPrice.calculateChangeRate(yesterdayPrice);
    }

    private int getChangeAmount(Stock stock, CurrentPrice currentPrice, LocalDate date) {
        CurrentPrice yesterdayPrice = currentPriceRepository.findByStockAndDate(stock, date.minusDays(1));
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
            CurrentPrice currentPrice = currentPriceRepository.findByStockAndDate(stock, date);
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
}
