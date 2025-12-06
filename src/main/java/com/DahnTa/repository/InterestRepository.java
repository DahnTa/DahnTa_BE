package com.DahnTa.repository;

import com.DahnTa.entity.Interest;
import com.DahnTa.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
    Optional<Interest> findByUserAndStockId(User user, Long stockId);
    boolean existsByUserAndStockId(User user, Long stockId);

    List<Interest> findAllByUser(User user);
    void deleteByUser(User user);
}
