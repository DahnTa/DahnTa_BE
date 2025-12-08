package com.DahnTa.repository;

import com.DahnTa.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserAccount(String userAccount);
    boolean existsByUserAccount(String userAccount);
}
