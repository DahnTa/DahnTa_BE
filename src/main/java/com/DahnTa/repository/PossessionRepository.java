package com.DahnTa.repository;

import com.DahnTa.entity.Possession;
import com.DahnTa.entity.Stock;
import com.DahnTa.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PossessionRepository extends JpaRepository<Possession, Long> {

    Optional<Possession> findByStockAndUser(Stock stock, User user);
    boolean existsByStockAnUser(Stock stock, User user);
    void deleteByUser(User user);
}
