package com.DahnTa.repository;

import com.DahnTa.entity.CompanyFinance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyFinanceRepository extends JpaRepository<CompanyFinance, Long> {

}
