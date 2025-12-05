package com.DahnTa.repository;

import com.DahnTa.entity.CurrentPrice;
import com.DahnTa.entity.Stock;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentPriceRepository extends JpaRepository<CurrentPrice, Long> {

    Optional<CurrentPrice> findByStockAndDate(Stock stock, LocalDate date);
    void deleteByUserId(Long userId);
}
