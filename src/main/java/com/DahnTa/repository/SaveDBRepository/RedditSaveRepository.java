package com.DahnTa.repository.SaveDBRepository;

import com.DahnTa.entity.saveDB.RedditSave;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RedditSaveRepository extends JpaRepository<RedditSave, Long> {

    List<RedditSave> findByDateBetween(LocalDate startDate, LocalDate endDate);
    Optional<RedditSave> findByStockIdAndDate(Long stockId, LocalDate date);

    Optional<RedditSave> findFirstByStockIdAndDateLessThanEqualOrderByDateDesc(Long stockId, LocalDate date);
}
