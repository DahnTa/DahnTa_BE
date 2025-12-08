package com.DahnTa.util;

import com.DahnTa.entity.CompanyFinance;
import com.DahnTa.entity.CurrentPrice;
import com.DahnTa.entity.MacroIndicators;
import com.DahnTa.entity.News;
import com.DahnTa.entity.Reddit;
import com.DahnTa.entity.Stock;
import com.DahnTa.entity.TotalAnalysis;
import com.DahnTa.entity.User;
import com.DahnTa.entity.saveDB.CompanyFinanceSave;
import com.DahnTa.entity.saveDB.CurrentPriceSave;
import com.DahnTa.entity.saveDB.MacroIndicatorsSave;
import com.DahnTa.entity.saveDB.NewsSave;
import com.DahnTa.entity.saveDB.RedditSave;
import com.DahnTa.entity.saveDB.TotalAnalysisSave;
import com.DahnTa.repository.CompanyFinanceRepository;
import com.DahnTa.repository.CurrentPriceRepository;
import com.DahnTa.repository.MacroIndicatorsRepository;
import com.DahnTa.repository.NewsRepository;
import com.DahnTa.repository.RedditRepository;
import com.DahnTa.repository.SaveDBRepository.CompanyFinanceSaveRepository;
import com.DahnTa.repository.SaveDBRepository.CurrentPriceSaveRepository;
import com.DahnTa.repository.SaveDBRepository.MacroIndicatorsSaveRepository;
import com.DahnTa.repository.SaveDBRepository.NewsSaveRepository;
import com.DahnTa.repository.SaveDBRepository.RedditSaveRepository;
import com.DahnTa.repository.SaveDBRepository.TotalAnalysisSaveRepository;
import com.DahnTa.repository.StockRepository;
import com.DahnTa.repository.TotalAnalysisRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SaveDBLoadUtil {

    private static final Map<String, String> STOCK_NAMES = Map.of(
        "microsoft", "마이크로소프트",
        "meta", "메타",
        "berkshire_hathaway", "버크셔 해서웨이",
        "broadcom", "브로드컴",
        "amazon", "아마존",
        "alphabet", "알파벳",
        "apple", "애플",
        "nvidia", "엔비디아",
        "walmart", "월마트",
        "tesla", "테슬라"
    );

    private final StockRepository stockRepository;
    private final CurrentPriceRepository currentPriceRepository;
    private final CompanyFinanceRepository companyFinanceRepository;
    private final MacroIndicatorsRepository macroIndicatorsRepository;
    private final NewsRepository newsRepository;
    private final RedditRepository redditRepository;
    private final TotalAnalysisRepository totalAnalysisRepository;

    private final CurrentPriceSaveRepository currentPriceSaveRepository;
    private final CompanyFinanceSaveRepository companyFinanceSaveRepository;
    private final MacroIndicatorsSaveRepository macroIndicatorsSaveRepository;
    private final NewsSaveRepository newsSaveRepository;
    private final RedditSaveRepository redditSaveRepository;
    private final TotalAnalysisSaveRepository totalAnalysisSaveRepository;

    public SaveDBLoadUtil(StockRepository stockRepository,
        CurrentPriceRepository currentPriceRepository,
        CompanyFinanceRepository companyFinanceRepository,
        MacroIndicatorsRepository macroIndicatorsRepository,
        NewsRepository newsRepository,
        RedditRepository redditRepository,
        TotalAnalysisRepository totalAnalysisRepository,
        CurrentPriceSaveRepository currentPriceSaveRepository,
        CompanyFinanceSaveRepository companyFinanceSaveRepository,
        MacroIndicatorsSaveRepository macroIndicatorsSaveRepository,
        NewsSaveRepository newsSaveRepository,
        RedditSaveRepository redditSaveRepository,
        TotalAnalysisSaveRepository totalAnalysisSaveRepository) {
        this.stockRepository = stockRepository;
        this.currentPriceRepository = currentPriceRepository;
        this.companyFinanceRepository = companyFinanceRepository;
        this.macroIndicatorsRepository = macroIndicatorsRepository;
        this.newsRepository = newsRepository;
        this.redditRepository = redditRepository;
        this.totalAnalysisRepository = totalAnalysisRepository;
        this.currentPriceSaveRepository = currentPriceSaveRepository;
        this.companyFinanceSaveRepository = companyFinanceSaveRepository;
        this.macroIndicatorsSaveRepository = macroIndicatorsSaveRepository;
        this.newsSaveRepository = newsSaveRepository;
        this.redditSaveRepository = redditSaveRepository;
        this.totalAnalysisSaveRepository = totalAnalysisSaveRepository;
    }

    @Transactional
    public void loadSaveDBForCurrentPrice(User user, LocalDate startDate, LocalDate endDate) {
        List<CurrentPriceSave> saveList = currentPriceSaveRepository.findByDateBetween(startDate, endDate);

        List<CurrentPrice> currentPrices = saveList.stream()
            .map(save -> {
                Stock stock = stockRepository.findById(save.getStockId())
                    .orElseThrow(() -> new IllegalArgumentException("Stock not found: " + save.getStockId()));
                return CurrentPrice.create(
                    stock,
                    save.getDate(),
                    save.getCurrentPrice(),
                    save.getMarketPrice(),
                    user.getId()
                );
            })
            .toList();

        currentPriceRepository.saveAll(currentPrices);
    }

    @Transactional
    public void loadSaveDBForNews(User user, LocalDate startDate, LocalDate endDate) {
        List<NewsSave> saveList = newsSaveRepository.findByDateBetween(startDate, endDate);

        List<News> newsList = saveList.stream()
            .map(save -> {
                Stock stock = stockRepository.findById(save.getStockId())
                    .orElseThrow(() -> new IllegalArgumentException("Stock not found: " + save.getStockId()));
                return News.create(
                    stock,
                    save.getDate(),
                    save.getContent(),
                    user.getId()
                );
            })
            .toList();

        newsRepository.saveAll(newsList);
    }

    @Transactional
    public void loadSaveDBForMacroIndicators(User user, LocalDate startDate, LocalDate endDate) {
        List<MacroIndicatorsSave> saveList = macroIndicatorsSaveRepository.findByDateBetween(startDate, endDate);

        List<MacroIndicators> macroIndicatorsList = saveList.stream()
            .map(save -> MacroIndicators.create(
                save.getDate(),
                save.getContent(),
                user.getId()
            ))
            .toList();

        macroIndicatorsRepository.saveAll(macroIndicatorsList);
    }

    @Transactional
    public void loadSaveDBForCompanyFinance(User user, LocalDate startDate, LocalDate endDate) {
        List<CompanyFinanceSave> saveList = companyFinanceSaveRepository.findByDateBetween(startDate, endDate);

        List<CompanyFinance> companyFinanceList = saveList.stream()
            .map(save -> {
                Stock stock = stockRepository.findById(save.getStockId())
                    .orElseThrow(() -> new IllegalArgumentException("Stock not found: " + save.getStockId()));
                return CompanyFinance.create(
                    stock,
                    save.getDate(),
                    save.getContent(),
                    user.getId()
                );
            })
            .toList();

        companyFinanceRepository.saveAll(companyFinanceList);
    }

    @Transactional
    public void loadSaveDBForTotalAnalysis(User user, LocalDate startDate, LocalDate endDate) {
        List<TotalAnalysisSave> saveList = totalAnalysisSaveRepository.findByDateBetween(startDate, endDate);

        List<TotalAnalysis> totalAnalysisList = saveList.stream()
            .map(save -> {
                Stock stock = stockRepository.findById(save.getStockId())
                    .orElseThrow(() -> new IllegalArgumentException("Stock not found: " + save.getStockId()));
                return TotalAnalysis.create(
                    stock,
                    save.getDate(),
                    save.getCompanyName(),
                    save.getAnalyze(),
                    user.getId()
                );
            })
            .toList();

        totalAnalysisRepository.saveAll(totalAnalysisList);
    }

    @Transactional
    public void loadSaveDBForReddit(User user, LocalDate startDate, LocalDate endDate) {
        List<RedditSave> saveList = redditSaveRepository.findByDateBetween(startDate, endDate);

        List<Reddit> redditList = saveList.stream()
            .map(save -> {
                Stock stock = stockRepository.findById(save.getStockId())
                    .orElseThrow(() -> new IllegalArgumentException("Stock not found: " + save.getStockId()));
                return Reddit.create(
                    stock,
                    save.getDate(),
                    save.getTitle(),
                    save.getContent(),
                    save.getScore(),
                    save.getNumComment(),
                    user.getId()
                );
            })
            .toList();

        redditRepository.saveAll(redditList);
    }
}