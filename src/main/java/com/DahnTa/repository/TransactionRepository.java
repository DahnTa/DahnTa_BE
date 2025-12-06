package com.DahnTa.repository;

import com.DahnTa.entity.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t JOIN FETCH t.stock WHERE t.user.id = :userId")
    List<Transaction> findAllByUserId(@Param("userId") Long userId);
}

