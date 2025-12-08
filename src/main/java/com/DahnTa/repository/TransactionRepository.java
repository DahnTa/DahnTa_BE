package com.DahnTa.repository;

import com.DahnTa.entity.Stock;
import com.DahnTa.entity.Transaction;
import com.DahnTa.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t JOIN FETCH t.stock WHERE t.user = :user")
    List<Transaction> findAllByUser(@Param("user") User user);
    List<Transaction> findByStockAndUser(Stock stock, User user);
    void deleteByUser(User user);
}

