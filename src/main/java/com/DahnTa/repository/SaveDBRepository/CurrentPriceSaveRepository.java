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

    // 좀 더 고도화 필요 -> 날짜 설정 때 current가 없는 날이면 넘기고 그만큼 날짜를 더하는 식으로, 나머지 repository도 current 기준으로 날짜 동일화
    Optional<CurrentPriceSave> findFirstByStockIdAndDateLessThanEqualOrderByDateDesc(Long stockId, LocalDate date);

    List<CurrentPriceSave> findTop2ByStockIdOrderByDateDesc(Long stockId);

    Optional<CurrentPriceSave> findTop1ByStockIdOrderByDateDesc(Long stockId);
}
