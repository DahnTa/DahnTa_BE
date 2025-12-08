package com.DahnTa.util;

import com.DahnTa.entity.User;
import com.DahnTa.repository.CompanyFinanceRepository;
import com.DahnTa.repository.CurrentPriceRepository;
import com.DahnTa.repository.GameDateRepository;
import com.DahnTa.repository.InterestRepository;
import com.DahnTa.repository.MacroIndicatorsRepository;
import com.DahnTa.repository.NewsRepository;
import com.DahnTa.repository.PossessionRepository;
import com.DahnTa.repository.RedditRepository;
import com.DahnTa.repository.TotalAnalysisRepository;
import com.DahnTa.repository.TransactionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class RemoveGameDataUtil {

    private final GameDateRepository gameDateRepository;
    private final PossessionRepository possessionRepository;
    private final CurrentPriceRepository currentPriceRepository;
    private final CompanyFinanceRepository companyFinanceRepository;
    private final MacroIndicatorsRepository macroIndicatorsRepository;
    private final NewsRepository newsRepository;
    private final RedditRepository redditRepository;
    private final TotalAnalysisRepository totalAnalysisRepository;
    private final InterestRepository interestRepository;
    private final TransactionRepository transactionRepository;

    public RemoveGameDataUtil(GameDateRepository gameDateRepository,
        PossessionRepository possessionRepository, CurrentPriceRepository currentPriceRepository,
        CompanyFinanceRepository companyFinanceRepository,
        MacroIndicatorsRepository macroIndicatorsRepository, NewsRepository newsRepository,
        RedditRepository redditRepository, TotalAnalysisRepository totalAnalysisRepository,
        InterestRepository interestRepository, TransactionRepository transactionRepository) {
        this.gameDateRepository = gameDateRepository;
        this.possessionRepository = possessionRepository;
        this.currentPriceRepository = currentPriceRepository;
        this.companyFinanceRepository = companyFinanceRepository;
        this.macroIndicatorsRepository = macroIndicatorsRepository;
        this.newsRepository = newsRepository;
        this.redditRepository = redditRepository;
        this.totalAnalysisRepository = totalAnalysisRepository;
        this.interestRepository = interestRepository;
        this.transactionRepository = transactionRepository;
    }

    public void gameDataRemoveByCurrentPrice(User user) {
        currentPriceRepository.deleteByUserId(user.getId());
    }

    public void gameDataRemoveByCompanyFinance(User user) {
        companyFinanceRepository.deleteByUserId(user.getId());
    }

    public void gameDataRemoveByMacroIndicators(User user) {
        macroIndicatorsRepository.deleteByUserId(user.getId());
    }

    public void gameDataRemoveByNews(User user) {
        newsRepository.deleteByUserId(user.getId());
    }

    public void gameDataRemoveByReddit(User user) {
        redditRepository.deleteByUserId(user.getId());
    }

    public void gameDataRemoveByTotalAnalysis(User user) {
        totalAnalysisRepository.deleteByUserId(user.getId());
    }

    public void gameDataRemoveByPossession(User user) {
        possessionRepository.deleteByUser(user);
    }

    public void gameDataRemoveByGameDate(User user) {
        gameDateRepository.deleteByUser(user);
    }

    public void gameDateRemoveByInterest(User user) {
        interestRepository.deleteByUser(user);
    }

    public void gameDateRemoveByTransaction(User user) {
        transactionRepository.deleteByUser(user);
    }
}
