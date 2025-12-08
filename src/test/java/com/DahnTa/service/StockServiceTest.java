package com.DahnTa.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.DahnTa.dto.DashBoard;
import com.DahnTa.dto.MarketPrices;
import com.DahnTa.dto.request.StockBuyRequest;
import com.DahnTa.dto.request.StockSellRequest;
import com.DahnTa.dto.response.*;
import com.DahnTa.entity.*;
import com.DahnTa.entity.Enum.ErrorCode;
import com.DahnTa.exception.StockException;
import com.DahnTa.repository.*;
import com.DahnTa.util.CsvLoadUtil;
import com.DahnTa.util.RemoveGameDataUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("StockService 단위 테스트")
class StockServiceTest {

    @Mock
    private GameDateRepository gameDateRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private PossessionRepository possessionRepository;

    @Mock
    private CurrentPriceRepository currentPriceRepository;

    @Mock
    private CompanyFinanceRepository companyFinanceRepository;

    @Mock
    private MacroIndicatorsRepository macroIndicatorsRepository;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private RedditRepository redditRepository;

    @Mock
    private TotalAnalysisRepository totalAnalysisRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CsvLoadUtil csvLoadUtil;

    @Mock
    private RemoveGameDataUtil removeGameDataUtil;

    @InjectMocks
    private StockService stockService;

    private User testUser;
    private Stock testStock;
    private GameDate testGameDate;
    private CurrentPrice testCurrentPrice;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2024, 1, 15);
        testUser = createTestUser();
        testStock = createTestStock();
        testGameDate = createTestGameDate();
        testCurrentPrice = createTestCurrentPrice();
    }

    @Test
    @DisplayName("게임 시작")
    void testGameStart_Success() {
        when(gameDateRepository.save(any(GameDate.class))).thenReturn(testGameDate);

        stockService.gameStart(testUser);

        verify(gameDateRepository, times(1)).save(any(GameDate.class));
        verify(csvLoadUtil, times(1)).loadCsvForCurrentPrice(eq(testUser), any(LocalDate.class),
            any(LocalDate.class));
        verify(csvLoadUtil, times(1)).loadCsvForNews(eq(testUser), any(LocalDate.class),
            any(LocalDate.class));
        verify(csvLoadUtil, times(1)).loadCsvForMacroIndicators(eq(testUser), any(LocalDate.class),
            any(LocalDate.class));
        verify(csvLoadUtil, times(1)).loadCsvForCompanyFinance(eq(testUser), any(LocalDate.class),
            any(LocalDate.class));
        verify(csvLoadUtil, times(1)).loadCsvForReddit(eq(testUser), any(LocalDate.class),
            any(LocalDate.class));
        verify(csvLoadUtil, times(1)).loadCsvForTotalAnalysis(eq(testUser), any(LocalDate.class),
            any(LocalDate.class));
    }

    @Test
    @DisplayName("게임 날짜 업데이트")
    void testGameDateNext_Success() {
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));

        stockService.gameDateNext(testUser);

        verify(gameDateRepository, times(1)).findByUser(testUser);
    }

    @Test
    @DisplayName("게임 종료")
    void testGameFinish_Success() {
        stockService.gameFinish(testUser);

        verify(removeGameDataUtil, times(1)).gameDataRemoveByCurrentPrice(testUser);
        verify(removeGameDataUtil, times(1)).gameDataRemoveByCompanyFinance(testUser);
        verify(removeGameDataUtil, times(1)).gameDataRemoveByMacroIndicators(testUser);
        verify(removeGameDataUtil, times(1)).gameDataRemoveByNews(testUser);
        verify(removeGameDataUtil, times(1)).gameDataRemoveByReddit(testUser);
        verify(removeGameDataUtil, times(1)).gameDataRemoveByTotalAnalysis(testUser);
        verify(removeGameDataUtil, times(1)).gameDataRemoveByPossession(testUser);
        verify(removeGameDataUtil, times(1)).gameDataRemoveByGameDate(testUser);
        verify(removeGameDataUtil, times(1)).gameDateRemoveByInterest(testUser);
        verify(removeGameDataUtil, times(1)).gameDateRemoveByTransaction(testUser);
    }

    @Test
    @DisplayName("주식 매수 - 기존 보유 없음")
    void testStockBuy_NoExistingPossession_Success() {
        int quantity = 10;
        double price = 100.0;
        StockBuyRequest request = new StockBuyRequest(quantity);

        testUser.increaseCredit(5000.0);

        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(currentPriceRepository.findByStockAndDate(eq(testStock), any(LocalDate.class)))
            .thenReturn(Optional.of(testCurrentPrice));
        when(possessionRepository.findByStockAndUser(testStock, testUser))
            .thenReturn(Optional.empty());

        stockService.stockBuy(testUser, testStock.getId(), request);

        verify(stockRepository, times(1)).findById(testStock.getId());
        verify(currentPriceRepository, times(1)).findByStockAndDate(eq(testStock), any(LocalDate.class));
        verify(possessionRepository, times(1)).save(any(Possession.class));
    }

    @Test
    @DisplayName("주식 매수 - 기존 보유 있음")
    void testStockBuy_ExistingPossession_Success() {
        int quantity = 10;
        double price = 100.0;
        StockBuyRequest request = new StockBuyRequest(quantity);

        testUser.increaseCredit(5000.0);
        Possession existingPossession = Possession.create(testStock, testUser, 5);

        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(currentPriceRepository.findByStockAndDate(eq(testStock), any(LocalDate.class)))
            .thenReturn(Optional.of(testCurrentPrice));
        when(possessionRepository.findByStockAndUser(testStock, testUser))
            .thenReturn(Optional.of(existingPossession));

        stockService.stockBuy(testUser, testStock.getId(), request);

        verify(stockRepository, times(1)).findById(testStock.getId());
        verify(possessionRepository, times(1)).findByStockAndUser(testStock, testUser);
        assertEquals(15, existingPossession.getQuantity());
    }

    @Test
    @DisplayName("주식 매수 - 잔액 부족")
    void testStockBuy_InsufficientFunds_ThrowsException() {
        int quantity = 1000;
        StockBuyRequest request = new StockBuyRequest(quantity);

        testUser.increaseCredit(100.0);

        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(currentPriceRepository.findByStockAndDate(eq(testStock), any(LocalDate.class)))
            .thenReturn(Optional.of(testCurrentPrice));

        assertThrows(StockException.class, () -> {
            stockService.stockBuy(testUser, testStock.getId(), request);
        });
    }

    @Test
    @DisplayName("주식 매수 - 존재하지 않는 주식")
    void testStockBuy_StockNotFound_ThrowsException() {
        StockBuyRequest request = new StockBuyRequest(10);

        when(stockRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(StockException.class, () -> {
            stockService.stockBuy(testUser, 999L, request);
        });
    }

    @Test
    @DisplayName("주식 매도")
    void testStockSell_Success() {
        int quantity = 5;
        StockSellRequest request = new StockSellRequest(quantity);

        Possession possession = Possession.create(testStock, testUser, 10);

        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(possessionRepository.existsByStockAndUser(testStock, testUser)).thenReturn(true);
        when(possessionRepository.findByStockAndUser(testStock, testUser))
            .thenReturn(Optional.of(possession));
        when(currentPriceRepository.findByStockAndDate(eq(testStock), any(LocalDate.class)))
            .thenReturn(Optional.of(testCurrentPrice));

        stockService.stockSell(testUser, testStock.getId(), request);

        assertEquals(5, possession.getQuantity());
    }

    @Test
    @DisplayName("주식 매도 - 모든 보유주 판매")
    void testStockSell_AllQuantity_Success() {
        int quantity = 10;
        StockSellRequest request = new StockSellRequest(quantity);

        Possession possession = Possession.create(testStock, testUser, 10);

        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(possessionRepository.existsByStockAndUser(testStock, testUser)).thenReturn(true);
        when(possessionRepository.findByStockAndUser(testStock, testUser))
            .thenReturn(Optional.of(possession));
        when(currentPriceRepository.findByStockAndDate(eq(testStock), any(LocalDate.class)))
            .thenReturn(Optional.of(testCurrentPrice));

        stockService.stockSell(testUser, testStock.getId(), request);

        verify(possessionRepository, times(1)).delete(possession);
    }

    @Test
    @DisplayName("주식 매도 - 보유 수량 부족")
    void testStockSell_InsufficientQuantity_ThrowsException() {
        int quantity = 20;
        StockSellRequest request = new StockSellRequest(quantity);

        Possession possession = Possession.create(testStock, testUser, 5);

        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(possessionRepository.existsByStockAndUser(testStock, testUser)).thenReturn(true);
        when(possessionRepository.findByStockAndUser(testStock, testUser))
            .thenReturn(Optional.of(possession));

        assertThrows(StockException.class, () -> {
            stockService.stockSell(testUser, testStock.getId(), request);
        });
    }

    @Test
    @DisplayName("주식 매도 - 보유하지 않은 주식")
    void testStockSell_PossessionNotFound_ThrowsException() {
        StockSellRequest request = new StockSellRequest(5);

        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(possessionRepository.existsByStockAndUser(testStock, testUser)).thenReturn(false);

        assertThrows(StockException.class, () -> {
            stockService.stockSell(testUser, testStock.getId(), request);
        });
    }

    @Test
    @DisplayName("주식 목록 조회")
    void testGetStockList_Success() {
        List<Stock> stocks = new ArrayList<>();
        stocks.add(testStock);

        when(stockRepository.findAll()).thenReturn(stocks);
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(currentPriceRepository.findByStockAndDate(eq(testStock), any(LocalDate.class)))
            .thenReturn(Optional.of(testCurrentPrice));

        StockListResponse response = stockService.getStockList(testUser);

        assertNotNull(response);
        verify(stockRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("주식 목록 조회 - 빈 목록")
    void testGetStockList_Empty_Success() {
        List<Stock> stocks = new ArrayList<>();

        when(stockRepository.findAll()).thenReturn(stocks);
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));

        StockListResponse response = stockService.getStockList(testUser);

        assertNotNull(response);
        verify(stockRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("주식 상세 조회")
    void testGetStock_Success() {
        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(currentPriceRepository.findByStockAndDate(eq(testStock), any(LocalDate.class)))
            .thenReturn(Optional.of(testCurrentPrice));

        StockResponse response = stockService.getStock(testUser, testStock.getId());

        assertNotNull(response);
        verify(stockRepository, times(1)).findById(testStock.getId());
    }

    @Test
    @DisplayName("주식 상세 조회 - 존재하지 않는 주식")
    void testGetStock_NotFound_ThrowsException() {
        when(stockRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(StockException.class, () -> {
            stockService.getStock(testUser, 999L);
        });
    }

    @Test
    @DisplayName("주식 주문 조회 - 보유 없음")
    void testGetStockOrder_NoPossession_Success() {
        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(possessionRepository.findByStockAndUser(testStock, testUser))
            .thenReturn(Optional.empty());
        when(currentPriceRepository.findByStockAndDate(eq(testStock), any(LocalDate.class)))
            .thenReturn(Optional.of(testCurrentPrice));
        when(transactionRepository.findByStockAndUser(testStock, testUser))
            .thenReturn(new ArrayList<>());

        StockOrderResponse response = stockService.getStockOrder(testUser, testStock.getId());

        assertNotNull(response);
    }

    @Test
    @DisplayName("주식 주문 조회 - 보유 있음")
    void testGetStockOrder_WithPossession_Success() {
        Possession possession = Possession.create(testStock, testUser, 10);

        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(possessionRepository.findByStockAndUser(testStock, testUser))
            .thenReturn(Optional.of(possession));
        when(currentPriceRepository.findByStockAndDate(eq(testStock), any(LocalDate.class)))
            .thenReturn(Optional.of(testCurrentPrice));
        when(transactionRepository.findByStockAndUser(testStock, testUser))
            .thenReturn(new ArrayList<>());

        StockOrderResponse response = stockService.getStockOrder(testUser, testStock.getId());

        assertNotNull(response);
    }

    @Test
    @DisplayName("주식 뉴스 조회")
    void testGetStockNews_Success() {
        News news = News.create(testStock, testDate, "테스트 뉴스", 1L);

        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(newsRepository.findByStockAndDate(eq(testStock), any(LocalDate.class)))
            .thenReturn(Optional.of(news));

        StockNewsResponse response = stockService.getStockNews(testUser, testStock.getId());

        assertNotNull(response);
        verify(newsRepository, times(1)).findByStockAndDate(eq(testStock), any(LocalDate.class));
    }

    @Test
    @DisplayName("주식 뉴스 조회 - 없음")
    void testGetStockNews_NotFound_ThrowsException() {
        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(newsRepository.findByStockAndDate(eq(testStock), any(LocalDate.class)))
            .thenReturn(Optional.empty());

        assertThrows(StockException.class, () -> {
            stockService.getStockNews(testUser, testStock.getId());
        });
    }

    @Test
    @DisplayName("회사 재무 정보 조회")
    void testGetStockCompanyFinance_Success() {
        CompanyFinance companyFinance = CompanyFinance.create(testStock, testDate, "재무 정보", 1L);

        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(companyFinanceRepository.findByStockAndDate(eq(testStock), any(LocalDate.class)))
            .thenReturn(Optional.of(companyFinance));

        StockCompanyFinanceResponse response = stockService.getStockCompanyFinance(testUser, testStock.getId());

        assertNotNull(response);
        verify(companyFinanceRepository, times(1)).findByStockAndDate(eq(testStock), any(LocalDate.class));
    }

    @Test
    @DisplayName("회사 재무 정보 조회 - 없음")
    void testGetStockCompanyFinance_NotFound_ThrowsException() {
        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(companyFinanceRepository.findByStockAndDate(eq(testStock), any(LocalDate.class)))
            .thenReturn(Optional.empty());

        assertThrows(StockException.class, () -> {
            stockService.getStockCompanyFinance(testUser, testStock.getId());
        });
    }

    @Test
    @DisplayName("거시 지표 조회")
    void testGetMacroIndicators_Success() {
        MacroIndicators macroIndicators = MacroIndicators.create(testDate, "거시 지표", 1L);

        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(macroIndicatorsRepository.findByDate(any(LocalDate.class)))
            .thenReturn(Optional.of(macroIndicators));

        MacroIndicatorsResponse response = stockService.getMacroIndicators(testUser);

        assertNotNull(response);
        verify(macroIndicatorsRepository, times(1)).findByDate(any(LocalDate.class));
    }

    @Test
    @DisplayName("거시 지표 조회 - 없음")
    void testGetMacroIndicators_NotFound_ThrowsException() {
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(macroIndicatorsRepository.findByDate(any(LocalDate.class)))
            .thenReturn(Optional.empty());

        assertThrows(StockException.class, () -> {
            stockService.getMacroIndicators(testUser);
        });
    }

    @Test
    @DisplayName("레딧 정보 조회")
    void testGetReddit_Success() {
        Reddit reddit = Reddit.create(testStock, testDate, "레딧 내용", 100, 50, 1L);

        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(redditRepository.findByStockAndDate(eq(testStock), any(LocalDate.class)))
            .thenReturn(Optional.of(reddit));

        StockRedditResponse response = stockService.getReddit(testUser, testStock.getId());

        assertNotNull(response);
        verify(redditRepository, times(1)).findByStockAndDate(eq(testStock), any(LocalDate.class));
    }

    @Test
    @DisplayName("레딧 정보 조회 - 없음")
    void testGetReddit_NotFound_ThrowsException() {
        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(redditRepository.findByStockAndDate(eq(testStock), any(LocalDate.class)))
            .thenReturn(Optional.empty());

        assertThrows(StockException.class, () -> {
            stockService.getReddit(testUser, testStock.getId());
        });
    }

    @Test
    @DisplayName("종합 분석 정보 조회")
    void testGetTotalAnalysis_Success() {
        TotalAnalysis totalAnalysis = TotalAnalysis.create(testStock, testDate, "회사", "분석", 1L);

        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(totalAnalysisRepository.findByStockAndDate(eq(testStock), any(LocalDate.class)))
            .thenReturn(Optional.of(totalAnalysis));

        StockTotalAnalysisResponse response = stockService.getTotalAnalysis(testUser, testStock.getId());

        assertNotNull(response);
        verify(totalAnalysisRepository, times(1)).findByStockAndDate(eq(testStock), any(LocalDate.class));
    }

    @Test
    @DisplayName("종합 분석 정보 조회 - 없음")
    void testGetTotalAnalysis_NotFound_ThrowsException() {
        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(totalAnalysisRepository.findByStockAndDate(eq(testStock), any(LocalDate.class)))
            .thenReturn(Optional.empty());

        assertThrows(StockException.class, () -> {
            stockService.getTotalAnalysis(testUser, testStock.getId());
        });
    }

    @Test
    @DisplayName("게임 결과 조회 - 보유 없음")
    void testGetGameResult_NoPossession_Success() {
        testUser.increaseCredit(10000.0);

        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(possessionRepository.findByUser(testUser)).thenReturn(new ArrayList<>());

        StockGameResultResponse response = stockService.gatGameResult(testUser);

        assertNotNull(response);
        assertEquals(10000.0, response.initialFunds());
    }

    @Test
    @DisplayName("게임 결과 조회 - 보유 있음")
    void testGetGameResult_WithPossession_Success() {
        testUser.increaseCredit(10000.0);
        Possession possession = Possession.create(testStock, testUser, 100);
        List<Possession> possessions = new ArrayList<>();
        possessions.add(possession);

        when(gameDateRepository.findByUser(testUser)).thenReturn(Optional.of(testGameDate));
        when(possessionRepository.findByUser(testUser)).thenReturn(possessions);
        when(currentPriceRepository.findByStockAndDate(eq(testStock), any(LocalDate.class)))
            .thenReturn(Optional.of(testCurrentPrice));

        StockGameResultResponse response = stockService.gatGameResult(testUser);

        assertNotNull(response);
    }

    private User createTestUser() {
        return User.builder()
            .userAccount("testAccount")
            .userPassword("password")
            .userNickName("testUser")
            .userCredit(10000.0)
            .userProfileImageUrl(null)
            .build();
    }

    private Stock createTestStock() {
        return Stock.create("APPLE", "AAPL");
    }

    private GameDate createTestGameDate() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        return GameDate.create(testUser, startDate, endDate, 0);
    }

    private CurrentPrice createTestCurrentPrice() {
        return CurrentPrice.create(testStock, testDate, 100.0, 100.0, 1L);
    }
}