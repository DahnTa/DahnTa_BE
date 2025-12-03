package com.DahnTa.repository;

import com.DahnTa.entity.News;
import com.DahnTa.entity.Stock;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    News findByStockAndDate(Stock stock, LocalDate date);
}
