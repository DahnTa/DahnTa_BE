package com.DahnTa.unit;

import com.DahnTa.entity.CompanyFinance;
import com.DahnTa.entity.CurrentPrice;
import com.DahnTa.entity.MacroIndicators;
import com.DahnTa.entity.News;
import com.DahnTa.entity.Reddit;
import com.DahnTa.entity.Stock;
import com.DahnTa.repository.CompanyFinanceRepository;
import com.DahnTa.repository.CurrentPriceRepository;
import com.DahnTa.repository.MacroIndicatorsRepository;
import com.DahnTa.repository.NewsRepository;
import com.DahnTa.repository.RedditRepository;
import com.DahnTa.repository.StockRepository;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class CsvLoadUtil {

    private static final List<String> STOCK_NAMES = Arrays.asList(
        "마이크로소프트", "메타", "버크셔 해서웨이", "브로드컴", "아마존", "알파벳", "애플", "엔비디아", "월마트", "테슬라");

    private final StockRepository stockRepository;
    private final CurrentPriceRepository currentPriceRepository;
    private final CompanyFinanceRepository companyFinanceRepository;
    private final MacroIndicatorsRepository macroIndicatorsRepository;
    private final NewsRepository newsRepository;
    private final RedditRepository redditRepository;

    public CsvLoadUtil(StockRepository stockRepository, CurrentPriceRepository currentPriceRepository,
        CompanyFinanceRepository companyFinanceRepository,
        MacroIndicatorsRepository macroIndicatorsRepository,
        NewsRepository newsRepository, RedditRepository redditRepository) {
        this.stockRepository = stockRepository;
        this.currentPriceRepository = currentPriceRepository;
        this.companyFinanceRepository = companyFinanceRepository;
        this.macroIndicatorsRepository = macroIndicatorsRepository;
        this.newsRepository = newsRepository;
        this.redditRepository = redditRepository;
    }

    private List<String[]> readFilteredCsv(String path, LocalDate start, LocalDate end) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            File file = resource.getFile();

            List<String> lines = Files.readAllLines(file.toPath());
            lines.remove(0);

            List<String[]> filtered = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.split(",");
                LocalDate date = LocalDate.parse(parts[0]);
                if (!date.isBefore(start) && !date.isAfter(end)) {
                    filtered.add(parts);
                }
            }

            return filtered;

        } catch (Exception e) {
            throw new RuntimeException("CSV 로딩 실패: " + path);
        }
    }

    public void loadCsvForCurrentPrice(User user, LocalDate start, LocalDate end) {
        for (String stockName : STOCK_NAMES) {
            Stock stock = stockRepository.findByStockName(stockName);
            List<String[]> rows = readFilteredCsv("csv/current_price/" + stockName + ".csv", start, end);

            for (String[] data : rows) {
                LocalDate date = LocalDate.parse(data[0]);
                double currentPrice = Double.parseDouble(data[(int) (Math.random() * 2 + 1)]);
                double marketPrice = Double.parseDouble(data[(int) (Math.random() * 4 + 3)]);
                currentPriceRepository.save(
                    CurrentPrice.create(stock, date, currentPrice, marketPrice, user.getUserId()));
            }
        }
    }

    public void loadCsvForNews(User user, LocalDate start, LocalDate end) {
        for (String stockName : STOCK_NAMES) {
            Stock stock = stockRepository.findByStockName(stockName);
            List<String[]> rows = readFilteredCsv("csv/news/" + stockName + ".csv", start, end);

            for (String[] data : rows) {
                LocalDate date = LocalDate.parse(data[0]);
                newsRepository.save(
                    News.create(stock, date, data[1], data[2], user.getUserId()));
            }
        }
    }

    public void loadCsvForMacroIndicators(User user, LocalDate start, LocalDate end) {
        List<String[]> rows = readFilteredCsv("csv/macro_indicators/거시경제_gemini_results.csv", start, end);

        for (String[] data : rows) {
            LocalDate date = LocalDate.parse(data[0]);
            macroIndicatorsRepository.save(
                MacroIndicators.create(date, data[1], data[2], user.getUserId()));
        }
    }

    public void loadCsvForCompanyFinance(User user, LocalDate start, LocalDate end) {
        for (String stockName : STOCK_NAMES) {
            Stock stock = stockRepository.findByStockName(stockName);
            List<String[]> rows = readFilteredCsv("csv/company_finance/" + stockName + ".csv", start, end);

            for (String[] data : rows) {
                LocalDate date = LocalDate.parse(data[0]);
                companyFinanceRepository.save(
                    CompanyFinance.create(stock, date, data[1], data[2], user.getUserId()));
            }
        }
    }

    public void loadCsvForReddit(User user, LocalDate start, LocalDate end) {
        for (String stockName : STOCK_NAMES) {
            Stock stock = stockRepository.findByStockName(stockName);
            List<String[]> rows = readFilteredCsv("csv/reddit/" + stockName + ".csv", start, end);

            for (String[] data : rows) {
                LocalDate date = LocalDate.parse(data[0]);
                redditRepository.save(
                    Reddit.create(stock, date, data[1], data[2], data[3], data[4], user.getUserId()));
            }
        }
    }
}
