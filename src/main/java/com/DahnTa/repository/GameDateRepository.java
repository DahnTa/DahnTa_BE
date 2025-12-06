package com.DahnTa.repository;

import com.DahnTa.entity.GameDate;
import com.DahnTa.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameDateRepository extends JpaRepository<GameDate, Long> {

    Optional<GameDate> findByUser(User user);
    void deleteByUser(User user);
}
