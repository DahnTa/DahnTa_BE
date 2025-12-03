package com.DahnTa.service;

import com.DahnTa.dto.DashBoard;
import com.DahnTa.dto.response.StockListResponse;
import com.DahnTa.entity.CurrentPrice;
import com.DahnTa.entity.GameDate;
import com.DahnTa.entity.Stock;
import com.DahnTa.repository.CompanyFinanceRepository;
import com.DahnTa.repository.CurrentPriceRepository;
import com.DahnTa.repository.GameDateRepository;
import com.DahnTa.repository.MacroIndicatorsRepository;
import com.DahnTa.repository.NewsRepository;
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
    private final CurrentPriceRepository currentPriceRepository;
    private final CompanyFinanceRepository companyFinanceRepository;
    private final MacroIndicatorsRepository macroIndicatorsRepository;
    private final NewsRepository newsRepository;
    private final RedditRepository redditRepository;
    private final CsvLoadUtil csvLoadUtil;

    public StockService(GameDateRepository gameDateRepository, StockRepository stockRepository,
        CurrentPriceRepository currentPriceRepository, CompanyFinanceRepository companyFinanceRepository,
        MacroIndicatorsRepository macroIndicatorsRepository, NewsRepository newsRepository,
        RedditRepository redditRepository, CsvLoadUtil csvLoadUtil) {
        this.gameDateRepository = gameDateRepository;
        this.stockRepository = stockRepository;
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

    public StockListResponse getStockListResponse() {
        List<Stock> stocks = stockRepository.findAll();

        GameDate gameDate = gameDateRepository.findByUser(user);
        int day = gameDate.getDay();
        LocalDate startDate = gameDate.getStartDate();
        LocalDate currentDate = startDate.plusDays(day);

        List<DashBoard> dashBoards = new ArrayList<>();
        for (Stock stock : stocks) {
            CurrentPrice currentPrice = currentPriceRepository.findByStockAndDate(stock, currentDate);
            DashBoard board = DashBoard.create(stock.getId(), stock.getStockName(), stock.getStockTag(),
                currentPrice.getCurrentPrice(), currentPrice.getMarketPrice(),
                getChangeRate(stock, currentPrice, currentDate),
                getChangeAmount(stock, currentPrice, currentDate));
            dashBoards.add(board);
        }

        return StockListResponse.create(dashBoards);
    }

    private void setGameInformation(User user, LocalDate randomStart, LocalDate randomEnd) {
        csvLoadUtil.loadCsvForCurrentPrice(user, randomStart, randomEnd);
        csvLoadUtil.loadCsvForNews(user, randomStart, randomEnd);
        csvLoadUtil.loadCsvForMacroIndicators(user, randomStart, randomEnd);
        csvLoadUtil.loadCsvForCompanyFinance(user, randomStart, randomEnd);
    }

    private double getChangeRate(Stock stock, CurrentPrice currentPrice, LocalDate date) {
        CurrentPrice yesterdayPrice = currentPriceRepository.findByStockAndDate(stock, date.minusDays(1));
        double today = currentPrice.getCurrentPrice();
        double yesterday = yesterdayPrice.getCurrentPrice();

        return ((today - yesterday) / yesterday) * 100.0;
    }

    private int getChangeAmount(Stock stock, CurrentPrice currentPrice, LocalDate date) {
        CurrentPrice yesterdayPrice = currentPriceRepository.findByStockAndDate(stock, date.minusDays(1));

        return currentPrice.getCurrentPrice() - yesterdayPrice.getCurrentPrice();
    }
}
