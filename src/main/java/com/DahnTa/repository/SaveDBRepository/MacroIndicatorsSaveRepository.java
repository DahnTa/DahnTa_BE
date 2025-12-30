package com.DahnTa.repository.SaveDBRepository;

import com.DahnTa.entity.saveDB.MacroIndicatorsSave;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MacroIndicatorsSaveRepository extends JpaRepository<MacroIndicatorsSave, Long> {

    List<MacroIndicatorsSave> findByDateBetween(LocalDate startDate, LocalDate endDate);
    Optional<MacroIndicatorsSave> findByDate(LocalDate date);

    Optional<MacroIndicatorsSave> findFirstByDateLessThanEqualOrderByDateDesc(LocalDate date);
}
