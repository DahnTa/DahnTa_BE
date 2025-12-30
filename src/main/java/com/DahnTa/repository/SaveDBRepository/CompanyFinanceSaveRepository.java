package com.DahnTa.repository.SaveDBRepository;

import com.DahnTa.entity.saveDB.CompanyFinanceSave;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyFinanceSaveRepository extends JpaRepository<CompanyFinanceSave, Long> {

    List<CompanyFinanceSave> findByDateBetween(LocalDate startDate, LocalDate endDate);
    Optional<CompanyFinanceSave> findByStockIdAndDate(Long stockId, LocalDate date);

    Optional<CompanyFinanceSave> findFirstByStockIdAndDateLessThanEqualOrderByDateDesc(Long stockId, LocalDate date);
}
