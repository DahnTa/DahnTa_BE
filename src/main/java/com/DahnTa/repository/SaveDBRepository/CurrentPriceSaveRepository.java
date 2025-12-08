package com.DahnTa.repository.SaveDBRepository;

import com.DahnTa.entity.saveDB.CurrentPriceSave;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentPriceSaveRepository extends JpaRepository<CurrentPriceSave, Long> {

    List<CurrentPriceSave> findByDateBetween(LocalDate startDate, LocalDate endDate);


    Optional<CurrentPriceSave> findFirstByStockIdAndDateLessThanEqualOrderByDateDesc(Long stockId, LocalDate date);

    List<CurrentPriceSave> findTop2ByStockIdOrderByDateDesc(Long stockId);

    Optional<CurrentPriceSave> findTop1ByStockIdOrderByDateDesc(Long stockId);
}
