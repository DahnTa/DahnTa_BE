package com.DahnTa.repository;

import com.DahnTa.entity.TotalAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalAnalysisRepository extends JpaRepository<TotalAnalysis, Long> {


}
