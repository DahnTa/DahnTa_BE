package com.DahnTa.repository;

import com.DahnTa.entity.MacroIndicators;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MacroIndicatorsRepository extends JpaRepository<MacroIndicators, Long> {

    MacroIndicators findByDate(LocalDate date);
}
