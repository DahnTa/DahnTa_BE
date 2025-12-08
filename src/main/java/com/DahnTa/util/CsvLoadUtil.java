package com.DahnTa.util;

import com.DahnTa.entity.CompanyFinance;
import com.DahnTa.entity.CurrentPrice;
import com.DahnTa.entity.Enum.ErrorCode;
import com.DahnTa.entity.MacroIndicators;
import com.DahnTa.entity.News;
import com.DahnTa.entity.Reddit;
import com.DahnTa.entity.Stock;
import com.DahnTa.entity.TotalAnalysis;
import com.DahnTa.entity.User;
import com.DahnTa.exception.StockException;
import com.DahnTa.repository.CompanyFinanceRepository;
import com.DahnTa.repository.CurrentPriceRepository;
import com.DahnTa.repository.MacroIndicatorsRepository;
import com.DahnTa.repository.NewsRepository;
import com.DahnTa.repository.RedditRepository;
import com.DahnTa.repository.StockRepository;
import com.DahnTa.repository.TotalAnalysisRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CsvLoadUtil {

    private static final DateTimeFormatter YYYYMMDD_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter YYYY_MM_DD_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

    public CsvLoadUtil(StockRepository stockRepository,
        CurrentPriceRepository currentPriceRepository,
        CompanyFinanceRepository companyFinanceRepository,
        MacroIndicatorsRepository macroIndicatorsRepository,
        NewsRepository newsRepository,
        RedditRepository redditRepository,
        TotalAnalysisRepository totalAnalysisRepository) {
        this.stockRepository = stockRepository;
        this.currentPriceRepository = currentPriceRepository;
        this.companyFinanceRepository = companyFinanceRepository;
        this.macroIndicatorsRepository = macroIndicatorsRepository;
        this.newsRepository = newsRepository;
        this.redditRepository = redditRepository;
        this.totalAnalysisRepository = totalAnalysisRepository;
    }

    private LocalDate parseDate(String dateStr) {
        String cleaned = dateStr.trim().replace("\"", "").replace(" ", "");

        try {
            return LocalDate.parse(cleaned, YYYYMMDD_FORMATTER);
        } catch (Exception e1) {
            try {
                return LocalDate.parse(cleaned, YYYY_MM_DD_FORMATTER);
            } catch (Exception e2) {
                throw new RuntimeException("날짜 파싱 실패: " + dateStr);
            }
        }
    }

    @Transactional
    public void loadCsvForCurrentPrice(User user, LocalDate start, LocalDate end) {
        System.out.println("\n===== CurrentPrice 로드 시작 =====");
        System.out.println("범위: " + start + " ~ " + end);

        for (String stockName : STOCK_NAMES.keySet()) {
            String koreanName = STOCK_NAMES.get(stockName);
            Stock stock = getStockByName(koreanName);
            System.out.println("\n[" + stockName + "] 로드 시작");

            try {
                ClassPathResource resource = new ClassPathResource("csv/current_price/" + stockName + ".csv");

                if (!resource.exists()) {
                    System.out.println("파일 없음!");
                    continue;
                }

                BufferedReader br = new BufferedReader(
                    new InputStreamReader(resource.getInputStream()));

                String line;
                boolean first = true;
                int count = 0;

                while ((line = br.readLine()) != null) {
                    if (first) {
                        System.out.println("헤더: " + line);
                        first = false;
                        continue;
                    }

                    String[] parts = line.split("\t", -1);
                    System.out.println("원본: " + line);
                    System.out.println("parts 길이: " + parts.length);

                    if (parts.length < 3) {
                        System.out.println("필드 부족!");
                        continue;
                    }

                    try {
                        LocalDate date = parseDate(parts[0]);
                        System.out.println("날짜: " + date + ", 범위 확인: " + (!date.isBefore(start) && !date.isAfter(end)));

                        if (!date.isBefore(start) && !date.isAfter(end)) {
                            double currentPrice = Double.parseDouble(parts[1].trim().replace("\"", ""));
                            double marketPrice = Double.parseDouble(parts[2].trim().replace("\"", ""));

                            System.out.println("저장: currentPrice=" + currentPrice + ", marketPrice=" + marketPrice);

                            CurrentPrice cp = CurrentPrice.create(stock, date, currentPrice, marketPrice, user.getId());
                            currentPriceRepository.save(cp);
                            count++;
                        }
                    } catch (Exception e) {
                        System.out.println("파싱 에러: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                br.close();
                System.out.println("저장 완료: " + count + "개\n");
            } catch (Exception e) {
                System.out.println("파일 로드 에러: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void loadCsvForNews(User user, LocalDate start, LocalDate end) {
        System.out.println("\n===== News 로드 시작 =====");
        System.out.println("범위: " + start + " ~ " + end);

        for (String stockName : STOCK_NAMES.keySet()) {
            Stock stock = getStockByName(STOCK_NAMES.get(stockName));
            System.out.println("\n[" + stockName + "] 로드 시작");

            try {
                ClassPathResource resource = new ClassPathResource("csv/news/" + stockName + ".csv");

                if (!resource.exists()) {
                    System.out.println("파일 없음!");
                    continue;
                }

                BufferedReader br = new BufferedReader(
                    new InputStreamReader(resource.getInputStream()));

                String line;
                boolean first = true;
                int count = 0;

                while ((line = br.readLine()) != null) {
                    if (first) {
                        System.out.println("헤더: " + line);
                        first = false;
                        continue;
                    }

                    String[] parts = line.split("\t", 3);
                    System.out.println("parts 길이: " + parts.length);

                    if (parts.length < 3) {
                        System.out.println("필드 부족!");
                        continue;
                    }

                    try {
                        LocalDate date = parseDate(parts[0]);
                        System.out.println("날짜: " + date);

                        if (!date.isBefore(start) && !date.isAfter(end)) {
                            String content = parts[2].trim().replace("\"", "");
                            System.out.println("내용 길이: " + content.length());

                            newsRepository.save(
                                News.create(stock, date, content, user.getId())
                            );
                            count++;
                        }
                    } catch (Exception e) {
                        System.out.println("파싱 에러: " + e.getMessage());
                    }
                }
                br.close();
                System.out.println("저장 완료: " + count + "개\n");
            } catch (Exception e) {
                System.out.println("파일 로드 에러: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void loadCsvForMacroIndicators(User user, LocalDate start, LocalDate end) {
        System.out.println("\n===== MacroIndicators 로드 시작 =====");
        System.out.println("범위: " + start + " ~ " + end);

        try {
            ClassPathResource resource = new ClassPathResource("csv/macro_indicators/MacroIndicators.csv");

            if (!resource.exists()) {
                System.out.println("파일 없음!");
                return;
            }

            BufferedReader br = new BufferedReader(
                new InputStreamReader(resource.getInputStream()));

            String line;
            boolean first = true;
            int count = 0;

            while ((line = br.readLine()) != null) {
                if (first) {
                    System.out.println("헤더: " + line);
                    first = false;
                    continue;
                }

                String[] parts = line.split("\t", 3);
                System.out.println("parts 길이: " + parts.length);

                if (parts.length < 3) {
                    System.out.println("필드 부족!");
                    continue;
                }

                try {
                    LocalDate date = parseDate(parts[0]);
                    System.out.println("날짜: " + date);

                    if (!date.isBefore(start) && !date.isAfter(end)) {
                        String content = parts[2].trim().replace("\"", "");
                        System.out.println("내용 길이: " + content.length());

                        macroIndicatorsRepository.save(
                            MacroIndicators.create(date, content, user.getId())
                        );
                        count++;
                    }
                } catch (Exception e) {
                    System.out.println("파싱 에러: " + e.getMessage());
                }
            }
            br.close();
            System.out.println("저장 완료: " + count + "개\n");
        } catch (Exception e) {
            System.out.println("파일 로드 에러: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Transactional
    public void loadCsvForCompanyFinance(User user, LocalDate start, LocalDate end) {
        System.out.println("\n===== CompanyFinance 로드 시작 =====");
        System.out.println("범위: " + start + " ~ " + end);

        for (String stockName : STOCK_NAMES.keySet()) {
            Stock stock = getStockByName(STOCK_NAMES.get(stockName));
            System.out.println("\n[" + stockName + "] 로드 시작");

            try {
                ClassPathResource resource = new ClassPathResource("csv/company_finance/" + stockName + ".csv");

                if (!resource.exists()) {
                    System.out.println("파일 없음!");
                    continue;
                }

                BufferedReader br = new BufferedReader(
                    new InputStreamReader(resource.getInputStream()));

                String line;
                boolean first = true;
                int count = 0;

                while ((line = br.readLine()) != null) {
                    if (first) {
                        System.out.println("헤더: " + line);
                        first = false;
                        continue;
                    }

                    String[] parts = line.split("\t", 3);
                    System.out.println("parts 길이: " + parts.length);

                    if (parts.length < 3) {
                        System.out.println("필드 부족!");
                        continue;
                    }

                    try {
                        LocalDate date = parseDate(parts[0]);
                        System.out.println("날짜: " + date);

                        if (!date.isBefore(start) && !date.isAfter(end)) {
                            String content = parts[2].trim().replace("\"", "");
                            System.out.println("내용 길이: " + content.length());

                            companyFinanceRepository.save(
                                CompanyFinance.create(stock, date, content, user.getId())
                            );
                            count++;
                        }
                    } catch (Exception e) {
                        System.out.println("파싱 에러: " + e.getMessage());
                    }
                }
                br.close();
                System.out.println("저장 완료: " + count + "개\n");
            } catch (Exception e) {
                System.out.println("파일 로드 에러: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void loadCsvForTotalAnalysis(User user, LocalDate start, LocalDate end) {
        System.out.println("\n===== TotalAnalysis 로드 시작 =====");
        System.out.println("범위: " + start + " ~ " + end);

        for (String stockName : STOCK_NAMES.keySet()) {
            Stock stock = getStockByName(STOCK_NAMES.get(stockName));
            System.out.println("\n[" + stockName + "] 로드 시작");

            try {
                ClassPathResource resource = new ClassPathResource("csv/total_analysis/" + stockName + ".csv");

                if (!resource.exists()) {
                    System.out.println("파일 없음!");
                    continue;
                }

                BufferedReader br = new BufferedReader(
                    new InputStreamReader(resource.getInputStream()));

                String line;
                boolean first = true;
                int count = 0;

                while ((line = br.readLine()) != null) {
                    if (first) {
                        System.out.println("헤더: " + line);
                        first = false;
                        continue;
                    }

                    String[] parts = line.split("\t", 3);
                    System.out.println("parts 길이: " + parts.length);

                    if (parts.length < 3) {
                        System.out.println("필드 부족!");
                        continue;
                    }

                    try {
                        LocalDate date = parseDate(parts[0]);
                        System.out.println("날짜: " + date);

                        if (!date.isBefore(start) && !date.isAfter(end)) {
                            String title = parts[1].trim().replace("\"", "");
                            String content = parts[2].trim().replace("\"", "");
                            System.out.println("제목 길이: " + title.length() + ", 내용 길이: " + content.length());

                            totalAnalysisRepository.save(
                                TotalAnalysis.create(stock, date, title, content, user.getId())
                            );
                            count++;
                        }
                    } catch (Exception e) {
                        System.out.println("파싱 에러: " + e.getMessage());
                    }
                }
                br.close();
                System.out.println("저장 완료: " + count + "개\n");
            } catch (Exception e) {
                System.out.println("파일 로드 에러: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void loadCsvForReddit(User user, LocalDate start, LocalDate end) {
        System.out.println("\n===== Reddit 로드 시작 =====");
        System.out.println("범위: " + start + " ~ " + end);

        for (String stockName : STOCK_NAMES.keySet()) {
            Stock stock = getStockByName(STOCK_NAMES.get(stockName));
            System.out.println("\n[" + stockName + "] 로드 시작");

            try {
                ClassPathResource resource = new ClassPathResource("csv/reddit/" + stockName + ".csv");

                if (!resource.exists()) {
                    System.out.println("파일 없음!");
                    continue;
                }

                BufferedReader br = new BufferedReader(
                    new InputStreamReader(resource.getInputStream()));

                String line;
                boolean first = true;
                int count = 0;

                while ((line = br.readLine()) != null) {
                    if (first) {
                        System.out.println("헤더: " + line);
                        first = false;
                        continue;
                    }

                    String[] parts = line.split("\t", -1);
                    System.out.println("parts 길이: " + parts.length);

                    if (parts.length < 5) {
                        System.out.println("필드 부족!");
                        continue;
                    }

                    try {
                        LocalDate date = parseDate(parts[0]);
                        System.out.println("날짜: " + date);

                        if (!date.isBefore(start) && !date.isAfter(end)) {
                            String title = parts[1].trim().replace("\"", "");
                            String selftext = parts[2].trim().replace("\"", "");

                            int score = 0;
                            if (!parts[3].trim().isEmpty() && !parts[3].trim().equals("score")) {
                                try {
                                    score = Integer.parseInt(parts[3].trim().replace("\"", ""));
                                } catch (Exception e) {
                                    score = 0;
                                }
                            }

                            int numComments = 0;
                            if (!parts[4].trim().isEmpty() && !parts[4].trim().equals("num_comments")) {
                                try {
                                    numComments = Integer.parseInt(parts[4].trim().replace("\"", ""));
                                } catch (Exception e) {
                                    numComments = 0;
                                }
                            }

                            System.out.println("제목: " + title + ", score: " + score + ", numComments: " + numComments);

                            redditRepository.save(
                                Reddit.create(stock, date, title, selftext, score, numComments, user.getId())
                            );
                            count++;
                        }
                    } catch (Exception e) {
                        System.out.println("파싱 에러: " + e.getMessage());
                    }
                }
                br.close();
                System.out.println("저장 완료: " + count + "개\n");
            } catch (Exception e) {
                System.out.println("파일 로드 에러: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private Stock getStockByName(String stockName) {
        return stockRepository.findByStockName(stockName)
            .orElseThrow(() -> new StockException(ErrorCode.STOCK_NOT_FOUND));
    }
}