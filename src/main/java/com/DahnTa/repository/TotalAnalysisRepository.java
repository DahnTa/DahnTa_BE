package com.DahnTa.repository;

import com.DahnTa.entity.Stock;
import com.DahnTa.entity.TotalAnalysis;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalAnalysisRepository extends JpaRepository<TotalAnalysis, Long> {

    Optional<TotalAnalysis> findByStockAndDate(Stock stock, LocalDate date);
}
