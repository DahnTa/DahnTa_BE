package com.DahnTa.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.DahnTa.Mapper.TransactionMapper;
import com.DahnTa.dto.response.AssetResponseDTO;
import com.DahnTa.dto.response.HoldingsListResponseDTO;
import com.DahnTa.dto.response.InterestResponseDTO;
import com.DahnTa.dto.response.TransactionListResponseDTO;
import com.DahnTa.dto.response.TransactionResponseDTO;
import com.DahnTa.entity.CurrentPrice;
import com.DahnTa.entity.Interest;
import com.DahnTa.entity.Possession;
import com.DahnTa.entity.Stock;
import com.DahnTa.entity.Transaction;
import com.DahnTa.entity.User;
import com.DahnTa.exception.UserStockException;
import com.DahnTa.repository.CurrentPriceRepository;
import com.DahnTa.repository.InterestRepository;
import com.DahnTa.repository.PossessionRepository;
import com.DahnTa.repository.StockRepository;
import com.DahnTa.repository.TransactionRepository;
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
@DisplayName("UserStockService 단위 테스트")
class UserStockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private InterestRepository interestRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CurrentPriceRepository currentPriceRepository;

    @Mock
    private PossessionRepository possessionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private UserStockService userStockService;

    private User testUser;
    private Stock testStock;
    private CurrentPrice testCurrentPrice;
    private Possession testPossession;
    private Interest testInterest;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2024, 1, 15);
        testUser = createTestUser();
        testStock = createTestStock();
        testCurrentPrice = createTestCurrentPrice();
        testPossession = createTestPossession();
        testInterest = createTestInterest();
    }

    @Test
    @DisplayName("보유 주식 조회 - 보유 있음")
    void testGetHoldings_WithPossession_Success() {
        List<Possession> possessions = new ArrayList<>();
        possessions.add(testPossession);

        when(possessionRepository.findAllByUser(testUser)).thenReturn(possessions);
        when(currentPriceRepository.findTop1ByStockIdOrderByDateDesc(testStock.getId()))
            .thenReturn(Optional.of(testCurrentPrice));

        HoldingsListResponseDTO response = userStockService.getHoldings(testUser);

        assertNotNull(response);
        verify(possessionRepository, times(1)).findAllByUser(testUser);
        verify(currentPriceRepository, times(1)).findTop1ByStockIdOrderByDateDesc(testStock.getId());
    }

    @Test
    @DisplayName("보유 주식 조회 - 보유 없음")
    void testGetHoldings_NoPossession_Success() {
        when(possessionRepository.findAllByUser(testUser)).thenReturn(new ArrayList<>());

        HoldingsListResponseDTO response = userStockService.getHoldings(testUser);

        assertNotNull(response);
        verify(possessionRepository, times(1)).findAllByUser(testUser);
    }

    @Test
    @DisplayName("자산 조회")
    void testGetAssets_Success() {
        List<Possession> possessions = new ArrayList<>();
        possessions.add(testPossession);

        testUser.increaseCredit(5000.0);

        when(possessionRepository.findAllByUser(testUser)).thenReturn(possessions);
        when(currentPriceRepository.findTop1ByStockIdOrderByDateDesc(testStock.getId()))
            .thenReturn(Optional.of(testCurrentPrice));

        AssetResponseDTO response = userStockService.getAssets(testUser);

        assertNotNull(response);
        assertTrue(response.totalAmount() > 0);
        verify(possessionRepository, times(1)).findAllByUser(testUser);
    }

    @Test
    @DisplayName("자산 조회 - 주식 없음")
    void testGetAssets_NoStock_Success() {
        testUser.increaseCredit(5000.0);

        when(possessionRepository.findAllByUser(testUser)).thenReturn(new ArrayList<>());

        AssetResponseDTO response = userStockService.getAssets(testUser);

        assertNotNull(response);
        assertEquals(testUser.getUserCredit(), response.userCredit());
        verify(possessionRepository, times(1)).findAllByUser(testUser);
    }

    @Test
    @DisplayName("관심 주식 목록 조회")
    void testGetInterestList_Success() {
        List<Interest> interests = new ArrayList<>();
        interests.add(testInterest);

        List<CurrentPrice> prices = new ArrayList<>();
        prices.add(testCurrentPrice);
        CurrentPrice yesterdayPrice = createYesterdayPrice();
        prices.add(yesterdayPrice);

        when(interestRepository.findAllByUser(testUser)).thenReturn(interests);
        when(currentPriceRepository.findTop2ByStockIdOrderByDateDesc(testStock.getId()))
            .thenReturn(prices);

        List<InterestResponseDTO> response = userStockService.getInterestList(testUser);

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(interestRepository, times(1)).findAllByUser(testUser);
        verify(currentPriceRepository, times(1)).findTop2ByStockIdOrderByDateDesc(testStock.getId());
    }

    @Test
    @DisplayName("관심 주식 목록 조회 - 1개만 있음")
    void testGetInterestList_OnlyOnePrice_Success() {
        List<Interest> interests = new ArrayList<>();
        interests.add(testInterest);

        List<CurrentPrice> prices = new ArrayList<>();
        prices.add(testCurrentPrice);

        when(interestRepository.findAllByUser(testUser)).thenReturn(interests);
        when(currentPriceRepository.findTop2ByStockIdOrderByDateDesc(testStock.getId()))
            .thenReturn(prices);

        List<InterestResponseDTO> response = userStockService.getInterestList(testUser);

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(interestRepository, times(1)).findAllByUser(testUser);
    }

    @Test
    @DisplayName("관심 주식 목록 조회 - 없음")
    void testGetInterestList_Empty_Success() {
        when(interestRepository.findAllByUser(testUser)).thenReturn(new ArrayList<>());

        List<InterestResponseDTO> response = userStockService.getInterestList(testUser);

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(interestRepository, times(1)).findAllByUser(testUser);
    }

    @Test
    @DisplayName("관심 주식 삭제 - 성공")
    void testApplyDislike_Success() {
        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(interestRepository.findByUserAndStockId(testUser, testStock.getId()))
            .thenReturn(Optional.of(testInterest));

        userStockService.applyDislike(testStock.getId(), testUser);

        verify(stockRepository, times(1)).findById(testStock.getId());
        verify(interestRepository, times(1)).findByUserAndStockId(testUser, testStock.getId());
        verify(interestRepository, times(1)).delete(testInterest);
    }

    @Test
    @DisplayName("관심 주식 삭제 - 주식 없음")
    void testApplyDislike_StockNotFound_ThrowsException() {
        when(stockRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserStockException.class, () -> {
            userStockService.applyDislike(999L, testUser);
        });
    }

    @Test
    @DisplayName("관심 주식 삭제 - 관심 없음")
    void testApplyDislike_InterestNotFound_ThrowsException() {
        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(interestRepository.findByUserAndStockId(testUser, testStock.getId()))
            .thenReturn(Optional.empty());

        assertThrows(UserStockException.class, () -> {
            userStockService.applyDislike(testStock.getId(), testUser);
        });
    }

    @Test
    @DisplayName("관심 주식 추가 - 성공")
    void testApplyLike_Success() {
        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(interestRepository.existsByUserAndStockId(testUser, testStock.getId()))
            .thenReturn(false);
        when(interestRepository.save(any(Interest.class))).thenReturn(testInterest);

        userStockService.applyLike(testStock.getId(), testUser);

        verify(stockRepository, times(1)).findById(testStock.getId());
        verify(interestRepository, times(1)).existsByUserAndStockId(testUser, testStock.getId());
        verify(interestRepository, times(1)).save(any(Interest.class));
    }

    @Test
    @DisplayName("관심 주식 추가 - 주식 없음")
    void testApplyLike_StockNotFound_ThrowsException() {
        when(stockRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserStockException.class, () -> {
            userStockService.applyLike(999L, testUser);
        });
    }

    @Test
    @DisplayName("관심 주식 추가 - 이미 관심 있음")
    void testApplyLike_AlreadyInterested_ThrowsException() {
        when(stockRepository.findById(testStock.getId())).thenReturn(Optional.of(testStock));
        when(interestRepository.existsByUserAndStockId(testUser, testStock.getId()))
            .thenReturn(true);

        assertThrows(UserStockException.class, () -> {
            userStockService.applyLike(testStock.getId(), testUser);
        });
    }

    @Test
    @DisplayName("거래 내역 조회")
    void testGetTransactionHistory_Success() {
        Transaction mockTransaction = mock(Transaction.class);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(mockTransaction);

        TransactionResponseDTO transactionDTO = mock(TransactionResponseDTO.class);

        when(transactionRepository.findAllByUser(testUser)).thenReturn(transactions);
        when(transactionMapper.toDTO(mockTransaction)).thenReturn(transactionDTO);

        TransactionListResponseDTO response = userStockService.getTransactionHistory(testUser);

        assertNotNull(response);
        verify(transactionRepository, times(1)).findAllByUser(testUser);
        verify(transactionMapper, times(1)).toDTO(mockTransaction);
    }

    @Test
    @DisplayName("거래 내역 조회 - 없음")
    void testGetTransactionHistory_Empty_Success() {
        when(transactionRepository.findAllByUser(testUser)).thenReturn(new ArrayList<>());

        TransactionListResponseDTO response = userStockService.getTransactionHistory(testUser);

        assertNotNull(response);
        verify(transactionRepository, times(1)).findAllByUser(testUser);
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

    private CurrentPrice createTestCurrentPrice() {
        return CurrentPrice.create(testStock, testDate, 150.0, 100.0, 1L);
    }

    private CurrentPrice createYesterdayPrice() {
        return CurrentPrice.create(testStock, testDate.minusDays(1), 140.0, 100.0, 1L);
    }

    private Possession createTestPossession() {
        return Possession.create(testStock, testUser, 10);
    }

    private Interest createTestInterest() {
        return new Interest(testUser, testStock);
    }
}