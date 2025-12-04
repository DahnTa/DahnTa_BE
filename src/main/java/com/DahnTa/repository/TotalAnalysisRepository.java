package com.DahnTa.repository;

import com.DahnTa.entity.Stock;
import com.DahnTa.entity.TotalAnalysis;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalAnalysisRepository extends JpaRepository<TotalAnalysis, Long> {

    TotalAnalysis findByStockAndDate(Stock stock, LocalDate date);
}
