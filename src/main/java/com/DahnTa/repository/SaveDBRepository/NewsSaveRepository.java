package com.DahnTa.repository.SaveDBRepository;

import com.DahnTa.entity.saveDB.NewsSave;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsSaveRepository extends JpaRepository<NewsSave, Long> {

    List<NewsSave> findByDateBetween(LocalDate startDate, LocalDate endDate);
    Optional<NewsSave> findByStockIdAndDate(Long stockId, LocalDate date);

    Optional<NewsSave> findFirstByStockIdAndDateLessThanEqualOrderByDateDesc(Long stockId, LocalDate date);
}
