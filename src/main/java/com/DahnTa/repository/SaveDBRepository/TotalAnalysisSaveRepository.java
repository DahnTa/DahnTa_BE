package com.DahnTa.repository.SaveDBRepository;

import com.DahnTa.entity.saveDB.TotalAnalysisSave;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalAnalysisSaveRepository extends JpaRepository<TotalAnalysisSave, Long> {

    List<TotalAnalysisSave> findByDateBetween(LocalDate startDate, LocalDate endDate);
    Optional<TotalAnalysisSave> findByStockIdAndDate(Long stockId, LocalDate date);
}
