package com.DahnTa.unit;

import com.DahnTa.entity.CurrentPrice;
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

    List<String> STOCK_NAMES = Arrays.asList(new String[]{"마이크로소프트", "메타", "버크셔 해서웨이",
        "브로드컴", "아마존", "알파벳", "애플", "엔비디아", "월마트", "테슬라"});

    private final StockRepository stockRepository;
    private final CurrentPriceRepository currentPriceRepository;
    private final CompanyFinanceRepository companyFinanceRepository;
    private final MacroIndicatorsRepository macroIndicatorsRepository;
    private final NewsRepository newsRepository;
    private final RedditRepository redditRepository;

    public CsvLoadUtil(StockRepository stockRepository, CurrentPriceRepository currentPriceRepository,
        CompanyFinanceRepository companyFinanceRepository,
        MacroIndicatorsRepository macroIndicatorsRepository, NewsRepository newsRepository,
        RedditRepository redditRepository) {
        this.stockRepository = stockRepository;
        this.currentPriceRepository = currentPriceRepository;
        this.companyFinanceRepository = companyFinanceRepository;
        this.macroIndicatorsRepository = macroIndicatorsRepository;
        this.newsRepository = newsRepository;
        this.redditRepository = redditRepository;
    }

    public void loadCsvForCurrentPrice(User user, LocalDate randomStart, LocalDate randomEnd) {
        try {
            for (String stockName : STOCK_NAMES) {
                ClassPathResource resource = new ClassPathResource("csv/current_price/" + stockName + ".csv");
                File csvFile = resource.getFile();
                List<String> lines = Files.readAllLines(csvFile.toPath());
                lines.remove(0);

                Stock stock = stockRepository.findByStockName(stockName);
                for (String line : lines) {
                    String[] data = line.split(",");
                    LocalDate date = LocalDate.parse(data[0]);
                    if (date.isBefore(randomStart) || date.isAfter(randomEnd)) {
                        continue;
                    }

                    double currentPrice = Double.parseDouble(data[(int)(Math.random()*2+1)]);
                    double marketPrice = Double.parseDouble(data[(int)(Math.random()*4+3)]);

                    CurrentPrice currentPriceEntity = CurrentPrice.create(stock, date, currentPrice,
                        marketPrice, user.getUserId());
                    currentPriceRepository.save(currentPriceEntity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("csv 로딩 실패");
        }
    }


}
