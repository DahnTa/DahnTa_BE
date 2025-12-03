package com.DahnTa.repository;

import com.DahnTa.entity.CurrentPrice;
import com.DahnTa.entity.Stock;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentPriceRepository extends JpaRepository<CurrentPrice, Long> {

    CurrentPrice findByStock(Stock stock);
    CurrentPrice findByStockAndDate(Stock stock, LocalDate date);
}
