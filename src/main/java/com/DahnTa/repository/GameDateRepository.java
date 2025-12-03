package com.DahnTa.repository;

import com.DahnTa.entity.GameDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameDateRepository extends JpaRepository<GameDate, Long> {

    GameDate findByUser(User user);
}
