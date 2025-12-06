package com.DahnTa.repository;

import com.DahnTa.entity.Possession;
import com.DahnTa.entity.Stock;
import com.DahnTa.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PossessionRepository extends JpaRepository<Possession, Long> {

    Optional<Possession> findByStockAndUser(Stock stock, User user);
    boolean existsByStockAndUser(Stock stock, User user);
    List<Possession> findAllByUser(User user);
    void deleteByUser(User user);
    List<Possession> findByUser(User user);
}
